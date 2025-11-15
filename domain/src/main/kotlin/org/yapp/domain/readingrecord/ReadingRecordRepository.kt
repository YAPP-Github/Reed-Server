package org.yapp.domain.readingrecord

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
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

    fun deleteById(id: UUID)

    /**
     * Find reading records created after the specified time for books owned by the user
     * 
     * @param userBookIds List of user book IDs to search in
     * @param after Find records created after this time
     * @return List of reading records matching the criteria
     */
    fun findByUserBookIdInAndCreatedAtAfter(userBookIds: List<UUID>, after: LocalDateTime): List<ReadingRecord>
}
