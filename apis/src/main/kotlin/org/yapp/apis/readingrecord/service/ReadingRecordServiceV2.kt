package org.yapp.apis.readingrecord.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequestV2
import org.yapp.apis.readingrecord.dto.request.UpdateReadingRecordRequestV2
import org.yapp.apis.readingrecord.dto.response.PrimaryEmotionDto
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponseV2
import org.yapp.apis.readingrecord.dto.response.ReadingRecordsWithPrimaryEmotionResponse
import org.yapp.domain.detailtag.DetailTagDomainService
import org.yapp.domain.readingrecord.PrimaryEmotion
import org.yapp.domain.readingrecord.ReadingRecord
import org.yapp.domain.readingrecord.ReadingRecordDomainService
import org.yapp.domain.readingrecord.ReadingRecordSortType
import org.yapp.domain.readingrecord.vo.ReadingRecordInfoVO
import org.yapp.domain.readingrecorddetailtag.ReadingRecordDetailTagDomainService
import org.yapp.domain.user.UserDomainService
import org.yapp.globalutils.annotation.ApplicationService
import java.util.*

@ApplicationService
class ReadingRecordServiceV2(
    private val readingRecordDomainService: ReadingRecordDomainService,
    private val detailTagDomainService: DetailTagDomainService,
    private val readingRecordDetailTagDomainService: ReadingRecordDetailTagDomainService,
    private val userDomainService: UserDomainService
) {
    @Transactional
    fun createReadingRecord(
        userId: UUID,
        userBookId: UUID,
        request: CreateReadingRecordRequestV2
    ): ReadingRecordResponseV2 {
        val primaryEmotion = request.validPrimaryEmotion()
        val detailEmotionTagIds = request.detailEmotionTagIds

        // Validate detail emotion tags belong to the selected primary emotion
        validateDetailEmotionTags(detailEmotionTagIds, primaryEmotion)

        // Create reading record
        val savedReadingRecord = readingRecordDomainService.createReadingRecordV2(
            userBookId = userBookId,
            pageNumber = request.pageNumber,
            quote = request.validQuote(),
            review = request.review,
            primaryEmotion = primaryEmotion
        )

        // Save detail emotion tags
        readingRecordDetailTagDomainService.createAndSaveAll(
            readingRecordId = savedReadingRecord.id.value,
            detailTagIds = detailEmotionTagIds
        )

        // Update user's lastActivity
        userDomainService.updateLastActivity(userId)

        return buildResponse(savedReadingRecord, detailEmotionTagIds)
    }

    @Transactional(readOnly = true)
    fun getReadingRecordDetail(
        readingRecordId: UUID
    ): ReadingRecordResponseV2 {
        val readingRecord = readingRecordDomainService.findById(readingRecordId)
        val detailTagIds = readingRecordDetailTagDomainService.findByReadingRecordId(readingRecordId)
            .map { it.detailTagId.value }

        return buildResponse(readingRecord, detailTagIds)
    }

    @Transactional(readOnly = true)
    fun getReadingRecordsByDynamicCondition(
        userBookId: UUID,
        sort: ReadingRecordSortType?,
        pageable: Pageable
    ): ReadingRecordsWithPrimaryEmotionResponse {
        val primaryEmotion = readingRecordDomainService.findPrimaryEmotionByUserBookId(userBookId)
        val primaryEmotionDto = toPrimaryEmotionDto(primaryEmotion)

        val readingRecordPage = readingRecordDomainService.findByDynamicCondition(userBookId, sort, pageable)
        if (readingRecordPage.isEmpty) {
            return ReadingRecordsWithPrimaryEmotionResponse.of(
                primaryEmotion = primaryEmotionDto,
                records = Page.empty(pageable)
            )
        }

        val readingRecordIds = readingRecordPage.content.map { it.id.value }
        val detailTagsMap = buildDetailTagsMap(readingRecordIds)
        val recordsPage = toResponsePage(readingRecordPage, detailTagsMap)

        return ReadingRecordsWithPrimaryEmotionResponse.of(
            primaryEmotion = primaryEmotionDto,
            records = recordsPage
        )
    }

    private fun toPrimaryEmotionDto(primaryEmotion: PrimaryEmotion?): PrimaryEmotionDto? =
        primaryEmotion?.let { PrimaryEmotionDto.of(code = it.name, displayName = it.displayName) }

    private fun buildDetailTagsMap(readingRecordIds: List<UUID>): Map<UUID, List<ReadingRecordInfoVO.DetailEmotionInfo>> {
        val detailTags = readingRecordDetailTagDomainService.findByReadingRecordIdIn(readingRecordIds)
        val tagLookup = detailTagDomainService
            .findAllById(detailTags.map { it.detailTagId.value }.distinct())
            .associateBy { it.id.value }

        return detailTags
            .groupBy { it.readingRecordId.value }
            .mapValues { (_, tags) ->
                tags.mapNotNull { tag ->
                    tagLookup[tag.detailTagId.value]?.let {
                        ReadingRecordInfoVO.DetailEmotionInfo(it.id.value, it.name)
                    }
                }
            }
    }

    private fun toResponsePage(
        readingRecordPage: Page<ReadingRecord>,
        detailTagsByRecordId: Map<UUID, List<ReadingRecordInfoVO.DetailEmotionInfo>>
    ): Page<ReadingRecordResponseV2> = readingRecordPage.map { record ->
        ReadingRecordResponseV2.from(
            ReadingRecordInfoVO.newInstance(
                readingRecord = record,
                detailEmotions = detailTagsByRecordId[record.id.value] ?: emptyList()
            )
        )
    }

    @Transactional
    fun updateReadingRecord(
        userId: UUID,
        readingRecordId: UUID,
        request: UpdateReadingRecordRequestV2
    ): ReadingRecordResponseV2 {
        val existingRecord = readingRecordDomainService.findById(readingRecordId)
        val newPrimaryEmotion = request.primaryEmotion ?: existingRecord.primaryEmotion
        val primaryEmotionChanged = isPrimaryEmotionChanged(request.primaryEmotion, existingRecord.primaryEmotion)

        // Validate detail emotion tags if provided
        if (!request.detailEmotionTagIds.isNullOrEmpty()) {
            validateDetailEmotionTags(request.detailEmotionTagIds, newPrimaryEmotion)
        }

        // Update reading record
        val savedReadingRecord = readingRecordDomainService.modifyReadingRecordV2(
            readingRecordId = readingRecordId,
            pageNumber = request.pageNumber,
            quote = request.quote,
            review = request.review,
            primaryEmotion = request.primaryEmotion
        )

        // Handle detail emotion tags
        val finalDetailTagIds = updateDetailEmotionTags(
            readingRecordId = readingRecordId,
            newDetailTagIds = request.detailEmotionTagIds,
            primaryEmotionChanged = primaryEmotionChanged
        )

        // Update user's lastActivity
        userDomainService.updateLastActivity(userId)

        return buildResponse(savedReadingRecord, finalDetailTagIds)
    }

    @Transactional
    fun deleteReadingRecord(readingRecordId: UUID) {
        readingRecordDetailTagDomainService.deleteAllByReadingRecordId(readingRecordId)
        readingRecordDomainService.deleteReadingRecordV2(readingRecordId)
    }

    private fun validateDetailEmotionTags(detailEmotionTagIds: List<UUID>, primaryEmotion: PrimaryEmotion) {
        if (detailEmotionTagIds.isEmpty()) return

        val detailTags = detailTagDomainService.findAllById(detailEmotionTagIds)
        require(detailTags.size == detailEmotionTagIds.size) {
            "Some detail emotion tag IDs are invalid"
        }
        require(detailTags.all { it.primaryEmotion == primaryEmotion }) {
            "All detail emotions must belong to the selected primary emotion"
        }
    }

    private fun isPrimaryEmotionChanged(
        requestPrimaryEmotion: PrimaryEmotion?,
        existingPrimaryEmotion: PrimaryEmotion
    ): Boolean {
        return requestPrimaryEmotion != null && requestPrimaryEmotion != existingPrimaryEmotion
    }

    private fun updateDetailEmotionTags(
        readingRecordId: UUID,
        newDetailTagIds: List<UUID>?,
        primaryEmotionChanged: Boolean
    ): List<UUID> {
        return when {
            newDetailTagIds != null -> {
                readingRecordDetailTagDomainService.deleteAllByReadingRecordId(readingRecordId)
                readingRecordDetailTagDomainService.createAndSaveAll(
                    readingRecordId = readingRecordId,
                    detailTagIds = newDetailTagIds
                )
                newDetailTagIds
            }

            primaryEmotionChanged -> {
                readingRecordDetailTagDomainService.deleteAllByReadingRecordId(readingRecordId)
                emptyList()
            }

            else -> {
                readingRecordDetailTagDomainService.findByReadingRecordId(readingRecordId)
                    .map { it.detailTagId.value }
            }
        }
    }

    private fun buildResponse(
        readingRecord: ReadingRecord,
        detailTagIds: List<UUID>
    ): ReadingRecordResponseV2 {
        val detailEmotions = if (detailTagIds.isNotEmpty()) {
            detailTagDomainService.findAllById(detailTagIds).map {
                ReadingRecordInfoVO.DetailEmotionInfo(it.id.value, it.name)
            }
        } else {
            emptyList()
        }

        return ReadingRecordResponseV2.from(
            ReadingRecordInfoVO.newInstance(
                readingRecord = readingRecord,
                detailEmotions = detailEmotions
            )
        )
    }

    @Transactional(readOnly = true)
    fun getUserBookIdByReadingRecordId(readingRecordId: UUID): UUID {
        return readingRecordDomainService.findById(readingRecordId).userBookId.value
    }
}
