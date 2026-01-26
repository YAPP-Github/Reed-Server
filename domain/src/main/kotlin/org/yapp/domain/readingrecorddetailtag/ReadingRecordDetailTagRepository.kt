package org.yapp.domain.readingrecorddetailtag

import java.util.*

interface ReadingRecordDetailTagRepository {
    fun findByReadingRecordId(readingRecordId: UUID): List<ReadingRecordDetailTag>
    fun findByReadingRecordIdIn(readingRecordIds: List<UUID>): List<ReadingRecordDetailTag>
    fun save(readingRecordDetailTag: ReadingRecordDetailTag): ReadingRecordDetailTag
    fun saveAll(readingRecordDetailTags: List<ReadingRecordDetailTag>): List<ReadingRecordDetailTag>
    fun deleteAllByReadingRecordId(readingRecordId: UUID)
}
