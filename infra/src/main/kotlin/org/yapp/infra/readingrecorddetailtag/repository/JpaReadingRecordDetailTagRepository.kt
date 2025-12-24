package org.yapp.infra.readingrecorddetailtag.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yapp.infra.readingrecorddetailtag.entity.ReadingRecordDetailTagEntity
import java.util.*

interface JpaReadingRecordDetailTagRepository : JpaRepository<ReadingRecordDetailTagEntity, UUID> {
    fun findByReadingRecordId(readingRecordId: UUID): List<ReadingRecordDetailTagEntity>
    fun findByReadingRecordIdIn(readingRecordIds: List<UUID>): List<ReadingRecordDetailTagEntity>
    fun deleteAllByReadingRecordId(readingRecordId: UUID)
}

