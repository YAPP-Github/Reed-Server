package org.yapp.infra.readingrecordtag.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yapp.infra.readingrecordtag.entity.ReadingRecordTagEntity
import java.util.UUID

interface JpaReadingRecordTagRepository : JpaRepository<ReadingRecordTagEntity, UUID> {
    fun findByReadingRecordId(readingRecordId: UUID): List<ReadingRecordTagEntity>
}
