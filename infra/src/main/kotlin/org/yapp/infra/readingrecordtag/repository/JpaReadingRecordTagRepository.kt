package org.yapp.infra.readingrecordtag.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yapp.infra.readingrecordtag.entity.ReadingRecordTagEntity
import java.util.*

interface JpaReadingRecordTagRepository : JpaRepository<ReadingRecordTagEntity, UUID>, JpaReadingRecordTagQuerydslRepository {
    fun findByReadingRecordId(readingRecordId: UUID): List<ReadingRecordTagEntity>
    fun findByReadingRecordIdIn(readingRecordIds: List<UUID>): List<ReadingRecordTagEntity>
    fun deleteAllByReadingRecordId(readingRecordId: UUID)
}
