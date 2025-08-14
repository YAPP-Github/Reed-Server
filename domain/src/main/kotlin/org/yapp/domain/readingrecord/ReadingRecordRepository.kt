package org.yapp.domain.readingrecord

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID


interface ReadingRecordRepository {

    fun save(readingRecord: ReadingRecord): ReadingRecord

    fun deleteAllByUserBookId(userBookId: UUID)

    fun findById(id: UUID): ReadingRecord?


    fun findAllByUserBookId(userBookId: UUID): List<ReadingRecord>


    fun findAllByUserBookId(userBookId: UUID, pageable: Pageable): Page<ReadingRecord>


    fun findAllByUserBookIdIn(userBookIds: List<UUID>): List<ReadingRecord>


    fun countByUserBookId(userBookId: UUID): Long


    fun findReadingRecordsByDynamicCondition(
        userBookId: UUID,
        sort: ReadingRecordSortType?,
        pageable: Pageable
    ): Page<ReadingRecord>
}
