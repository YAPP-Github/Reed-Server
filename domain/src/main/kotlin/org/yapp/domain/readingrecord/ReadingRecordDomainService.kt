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
import org.yapp.domain.userbook.UserBookRepository
import org.yapp.globalutils.annotation.DomainService
import java.util.UUID

import org.yapp.domain.userbook.exception.UserBookNotFoundException
import org.yapp.domain.userbook.exception.UserBookErrorCode

@DomainService
class ReadingRecordDomainService(
    private val readingRecordRepository: ReadingRecordRepository,
    private val tagRepository: TagRepository,
    private val readingRecordTagRepository: ReadingRecordTagRepository,
    private val userBookRepository: UserBookRepository
) {

    fun createReadingRecord(
        userBookId: UUID,
        pageNumber: Int,
        quote: String,
        review: String,
        emotionTags: List<String>
    ): ReadingRecordInfoVO {
        val userBook = userBookRepository.findById(userBookId)
            ?: throw UserBookNotFoundException(
                UserBookErrorCode.USER_BOOK_NOT_FOUND,
                "User book not found with id: $userBookId"
            )

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
        val readingRecord = readingRecordRepository.findById(readingRecordId)
            ?: throw ReadingRecordNotFoundException(
                ReadingRecordErrorCode.READING_RECORD_NOT_FOUND,
                "Reading record not found with id: $readingRecordId"
            )

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
        val readingRecords = readingRecordPage.content

        if (readingRecords.isEmpty()) {
            return Page.empty(pageable)
        }

        val readingRecordIds = readingRecords.map { it.id.value }
        val tagIds = readingRecordTags.map { it.tagId.value }.distinct()
        val tags = tagRepository.findByIds(tagIds).associateBy { it.id.value }
        val userBook = userBookRepository.findById(userBookId)

        val readingRecordTagsByReadingRecordId = readingRecordTags.groupBy { it.readingRecordId.value }

        return readingRecordPage.map { readingRecord ->
            val currentReadingRecordTags = readingRecordTagsByReadingRecordId[readingRecord.id.value] ?: emptyList()
            val currentTags = currentReadingRecordTags.mapNotNull { tags[it.tagId.value] }

            ReadingRecordInfoVO.newInstance(
                readingRecord = readingRecord,
                emotionTags = currentTags.map { it.name },
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
        val readingRecord = readingRecordRepository.findById(readingRecordId)
            ?: throw ReadingRecordNotFoundException(
                ReadingRecordErrorCode.READING_RECORD_NOT_FOUND,
                "Reading record not found with id: $readingRecordId"
            )

        val updatedReadingRecord = readingRecord.update(
            pageNumber = pageNumber,
            quote = quote,
            review = review,
            emotionTags = emotionTags
        )

        val savedReadingRecord = readingRecordRepository.save(updatedReadingRecord)

        // Update emotion tags
        if (emotionTags != null) {
            readingRecordTagRepository.deleteAllByReadingRecordId(readingRecordId)
            val tags = emotionTags.map { tagName ->
                tagRepository.findByName(tagName) ?: tagRepository.save(Tag.create(tagName))
            }
            val newReadingRecordTags = tags.map {
                ReadingRecordTag.create(
                    readingRecordId = savedReadingRecord.id.value,
                    tagId = it.id.value
                )
            }
            readingRecordTagRepository.saveAll(newReadingRecordTags)
        }

        return buildReadingRecordInfoVO(savedReadingRecord)
    }

    fun deleteAllByUserBookId(userBookId: UUID) {
        readingRecordRepository.deleteAllByUserBookId(userBookId)
    }
    fun deleteReadingRecord(readingRecordId: UUID) {
        val readingRecord = readingRecordRepository.findById(readingRecordId)
            ?: throw ReadingRecordNotFoundException(
                ReadingRecordErrorCode.READING_RECORD_NOT_FOUND,
                "Reading record not found with id: $readingRecordId"
            )

        val userBook = userBookRepository.findById(readingRecord.userBookId.value)
            ?: throw UserBookNotFoundException(
                UserBookErrorCode.USER_BOOK_NOT_FOUND,
                "User book not found with id: ${readingRecord.userBookId.value}"
            )

        readingRecordRepository.deleteById(readingRecordId)
        userBookRepository.save(userBook.decreaseReadingRecordCount())
    }
}
