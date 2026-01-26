package org.yapp.infra.readingrecorddetailtag.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.yapp.infra.readingrecorddetailtag.entity.ReadingRecordDetailTagEntity
import java.util.*

interface JpaReadingRecordDetailTagRepository : JpaRepository<ReadingRecordDetailTagEntity, UUID> {
    fun findByReadingRecordId(readingRecordId: UUID): List<ReadingRecordDetailTagEntity>
    fun findByReadingRecordIdIn(readingRecordIds: List<UUID>): List<ReadingRecordDetailTagEntity>

    @Modifying
    @Query("DELETE FROM ReadingRecordDetailTagEntity e WHERE e.readingRecordId = :readingRecordId")
    fun deleteAllByReadingRecordId(@Param("readingRecordId") readingRecordId: UUID)
}

