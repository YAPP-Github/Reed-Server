package org.yapp.domain.readingrecord

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.yapp.domain.readingrecord.vo.ReadingRecordInfoVO
import org.yapp.domain.readingrecordtag.ReadingRecordTag
import org.yapp.domain.readingrecordtag.ReadingRecordTagRepository
import org.yapp.domain.tag.Tag
import org.yapp.domain.tag.TagRepository
import org.yapp.globalutils.annotation.DomainService
import java.util.UUID

@DomainService
class ReadingRecordDomainService(
    private val readingRecordRepository: ReadingRecordRepository,
    private val tagRepository: TagRepository,
    private val readingRecordTagRepository: ReadingRecordTagRepository
) {

    fun createReadingRecord(
        userBookId: UUID,
        pageNumber: Int,
        quote: String,
        review: String,
        emotionTags: List<String>
    ): ReadingRecordInfoVO {

        val readingRecord = ReadingRecord.create(
            userBookId = userBookId,
            pageNumber = pageNumber,
            quote = quote,
            review = review
        )

        val savedReadingRecord = readingRecordRepository.save(readingRecord)

        val tags = emotionTags.map { tagName ->
            tagRepository.findByName(tagName) ?: tagRepository.save(Tag.create(tagName))
        }

        val readingRecordTags = tags.map {
            ReadingRecordTag.create(
                readingRecordId = savedReadingRecord.id.value,
                tagId = it.id.value
            )
        }
        readingRecordTagRepository.saveAll(readingRecordTags)

        return ReadingRecordInfoVO.newInstance(savedReadingRecord, tags.map { it.name })
    }

    fun findReadingRecordsByDynamicCondition(
        userBookId: UUID,
        sort: ReadingRecordSortType?,
        pageable: Pageable
    ): Page<ReadingRecordInfoVO> {
        val page = readingRecordRepository.findReadingRecordsByDynamicCondition(userBookId, sort, pageable)
        return page.map { readingRecord ->
            val readingRecordTags = readingRecordTagRepository.findByReadingRecordId(readingRecord.id.value)
            val tagIds = readingRecordTags.map { it.tagId.value }
            val tags = tagRepository.findByIds(tagIds)
            ReadingRecordInfoVO.newInstance(readingRecord, tags.map { it.name })
        }
    }

}
