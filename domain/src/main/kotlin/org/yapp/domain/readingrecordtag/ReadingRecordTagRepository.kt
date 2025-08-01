package org.yapp.domain.readingrecordtag

import java.util.UUID

interface ReadingRecordTagRepository {
    fun saveAll(readingRecordTags: List<ReadingRecordTag>): List<ReadingRecordTag>
    fun findByReadingRecordId(readingRecordId: UUID): List<ReadingRecordTag>
    fun countTagsByUserIdAndCategories(userId: UUID, categories: List<String>): Map<String, Int>
}
