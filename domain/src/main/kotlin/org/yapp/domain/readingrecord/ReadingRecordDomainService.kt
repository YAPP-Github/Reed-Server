package org.yapp.domain.readingrecord

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.yapp.domain.readingrecord.exception.ReadingRecordErrorCode
import org.yapp.domain.readingrecord.exception.ReadingRecordNotFoundException
import org.yapp.domain.readingrecord.vo.ReadingRecordInfoVO
import org.yapp.domain.readingrecordtag.ReadingRecordTag
import org.yapp.domain.readingrecordtag.ReadingRecordTagRepository
import org.yapp.domain.tag.Tag
import org.yapp.domain.tag.TagRepository
import org.yapp.domain.userbook.UserBook
import org.yapp.domain.userbook.UserBookRepository
import org.yapp.domain.userbook.exception.UserBookErrorCode
import org.yapp.domain.userbook.exception.UserBookNotFoundException
import org.yapp.globalutils.annotation.DomainService
import java.util.UUID

@DomainService
class ReadingRecordDomainService(
    private val readingRecordRepository: ReadingRecordRepository,
    private val tagRepository: TagRepository,
    private val readingRecordTagRepository: ReadingRecordTagRepository,
    private val userBookRepository: UserBookRepository
) {
    // ===================== V2 API (Simple CRUD) =====================

    fun createReadingRecordV2(
        userBookId: UUID,
        pageNumber: Int?,
        quote: String,
        review: String?,
        primaryEmotion: PrimaryEmotion
    ): ReadingRecord {
        val userBook = findUserBookOrThrow(userBookId)

        val readingRecord = ReadingRecord.create(
            userBookId = userBookId,
            pageNumber = pageNumber,
            quote = quote,
            review = review,
            primaryEmotion = primaryEmotion
        )

        val savedReadingRecord = readingRecordRepository.save(readingRecord)
        userBookRepository.save(userBook.increaseReadingRecordCount())

        return savedReadingRecord
    }

    fun modifyReadingRecordV2(
        readingRecordId: UUID,
        pageNumber: Int?,
        quote: String?,
        review: String?,
        primaryEmotion: PrimaryEmotion?
    ): ReadingRecord {
        val readingRecord = findReadingRecordOrThrow(readingRecordId)

        val updatedReadingRecord = readingRecord.update(
            pageNumber = pageNumber,
            quote = quote,
            review = review,
            primaryEmotion = primaryEmotion,
            emotionTags = null
        )

        return readingRecordRepository.save(updatedReadingRecord)
    }

    fun findById(readingRecordId: UUID): ReadingRecord = findReadingRecordOrThrow(readingRecordId)

    fun findByDynamicCondition(
        userBookId: UUID,
        sort: ReadingRecordSortType?,
        pageable: Pageable
    ): Page<ReadingRecord> =
        readingRecordRepository.findReadingRecordsByDynamicCondition(userBookId, sort, pageable)

    fun deleteReadingRecordV2(readingRecordId: UUID) {
        val readingRecord = findReadingRecordOrThrow(readingRecordId)
        val userBook = findUserBookOrThrow(readingRecord.userBookId.value)

        readingRecordRepository.deleteById(readingRecordId)
        userBookRepository.save(userBook.decreaseReadingRecordCount())
    }

    fun findPrimaryEmotionByUserBookId(userBookId: UUID): PrimaryEmotion? =
        readingRecordRepository.findMostFrequentPrimaryEmotion(userBookId)

    fun countPrimaryEmotionsByUserBookId(userBookId: UUID): Map<PrimaryEmotion, Int> =
        readingRecordRepository.countPrimaryEmotionsByUserBookId(userBookId)

    // ===================== V1 API (Legacy) =====================

    fun createReadingRecord(
        userBookId: UUID,
        pageNumber: Int,
        quote: String,
        review: String?,
        emotionTags: List<String>
    ): ReadingRecordInfoVO {
        val userBook = findUserBookOrThrow(userBookId)

        val primaryEmotion = emotionTags.firstOrNull()?.let {
            PrimaryEmotion.fromDisplayName(it)
        } ?: PrimaryEmotion.OTHER

        val readingRecord = ReadingRecord.create(
            userBookId = userBookId,
            pageNumber = pageNumber,
            quote = quote,
            review = review,
            primaryEmotion = primaryEmotion
        )

        val savedReadingRecord = readingRecordRepository.save(readingRecord)

        val tags = findOrCreateTags(emotionTags)
        saveReadingRecordTags(savedReadingRecord.id.value, tags)

        userBookRepository.save(userBook.increaseReadingRecordCount())

        return ReadingRecordInfoVO.newInstance(
            readingRecord = savedReadingRecord,
            emotionTags = tags.map { it.name },
            bookTitle = userBook.title,
            bookPublisher = userBook.publisher,
            bookCoverImageUrl = userBook.coverImageUrl,
            author = userBook.author
        )
    }

    fun findReadingRecordById(readingRecordId: UUID): ReadingRecordInfoVO {
        val readingRecord = findReadingRecordOrThrow(readingRecordId)
        return buildReadingRecordInfoVO(readingRecord)
    }

    private fun buildReadingRecordInfoVO(readingRecord: ReadingRecord): ReadingRecordInfoVO {
        val readingRecordTags = readingRecordTagRepository.findByReadingRecordId(readingRecord.id.value)
        val tagIds = readingRecordTags.map { it.tagId.value }
        val tags = tagRepository.findByIds(tagIds)
        val userBook = userBookRepository.findById(readingRecord.userBookId.value)

        return ReadingRecordInfoVO.newInstance(
            readingRecord = readingRecord,
            emotionTags = tags.map { it.name },
            bookTitle = userBook?.title,
            bookPublisher = userBook?.publisher,
            bookCoverImageUrl = userBook?.coverImageUrl,
            author = userBook?.author
        )
    }

    fun findReadingRecordsByDynamicCondition(
        userBookId: UUID,
        sort: ReadingRecordSortType?,
        pageable: Pageable
    ): Page<ReadingRecordInfoVO> {
        val readingRecordPage = readingRecordRepository.findReadingRecordsByDynamicCondition(userBookId, sort, pageable)
        if (readingRecordPage.isEmpty) {
            return Page.empty(pageable)
        }

        val readingRecords = readingRecordPage.content.toList()
        val readingRecordIds = readingRecords.map { it.id.value }

        val readingRecordTags = readingRecordTagRepository.findByReadingRecordIdIn(readingRecordIds)
        val tagIds = readingRecordTags.map { it.tagId.value }
        val tagsById = tagRepository.findByIds(tagIds).associateBy { it.id.value }

        val tagsByReadingRecordId = readingRecordTags
            .groupBy { it.readingRecordId.value }
            .mapValues { (_, tags) ->
                tags.mapNotNull { tagsById[it.tagId.value] }
            }

        val userBook = userBookRepository.findById(userBookId)

        return readingRecordPage.map { readingRecord ->
            ReadingRecordInfoVO.newInstance(
                readingRecord = readingRecord,
                emotionTags = tagsByReadingRecordId[readingRecord.id.value]?.map { it.name } ?: emptyList(),
                bookTitle = userBook?.title,
                bookPublisher = userBook?.publisher,
                bookCoverImageUrl = userBook?.coverImageUrl,
                author = userBook?.author
            )
        }
    }

    fun modifyReadingRecord(
        readingRecordId: UUID,
        pageNumber: Int?,
        quote: String?,
        review: String?,
        emotionTags: List<String>?
    ): ReadingRecordInfoVO {
        val readingRecord = findReadingRecordOrThrow(readingRecordId)

        val primaryEmotion = emotionTags?.firstOrNull()?.let {
            PrimaryEmotion.fromDisplayName(it)
        }

        val updatedReadingRecord = readingRecord.update(
            pageNumber = pageNumber,
            quote = quote,
            review = review,
            primaryEmotion = primaryEmotion,
            emotionTags = emotionTags
        )

        val savedReadingRecord = readingRecordRepository.save(updatedReadingRecord)

        if (emotionTags != null) {
            readingRecordTagRepository.deleteAllByReadingRecordId(readingRecordId)
            val tags = findOrCreateTags(emotionTags)
            saveReadingRecordTags(savedReadingRecord.id.value, tags)
        }

        return buildReadingRecordInfoVO(savedReadingRecord)
    }

    fun deleteAllByUserBookId(userBookId: UUID) {
        readingRecordRepository.deleteAllByUserBookId(userBookId)
    }

    /**
     * V1 Legacy delete - delegates to V2 implementation
     */
    fun deleteReadingRecord(readingRecordId: UUID) = deleteReadingRecordV2(readingRecordId)

    // ===================== Private Helper Methods =====================

    private fun findReadingRecordOrThrow(id: UUID): ReadingRecord =
        readingRecordRepository.findById(id)
            ?: throw ReadingRecordNotFoundException(
                ReadingRecordErrorCode.READING_RECORD_NOT_FOUND,
                "Reading record not found with id: $id"
            )

    private fun findUserBookOrThrow(id: UUID): UserBook =
        userBookRepository.findById(id)
            ?: throw UserBookNotFoundException(
                UserBookErrorCode.USER_BOOK_NOT_FOUND,
                "User book not found with id: $id"
            )

    private fun findOrCreateTags(tagNames: List<String>): List<Tag> =
        tagNames.map { tagName ->
            tagRepository.findByName(tagName) ?: tagRepository.save(Tag.create(tagName))
        }

    private fun saveReadingRecordTags(readingRecordId: UUID, tags: List<Tag>) {
        val readingRecordTags = tags.map {
            ReadingRecordTag.create(readingRecordId = readingRecordId, tagId = it.id.value)
        }
        readingRecordTagRepository.saveAll(readingRecordTags)
    }

}

