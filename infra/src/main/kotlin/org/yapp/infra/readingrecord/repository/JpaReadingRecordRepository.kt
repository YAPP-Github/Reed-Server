package org.yapp.infra.readingrecord.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.yapp.infra.readingrecord.entity.ReadingRecordEntity
import java.time.LocalDateTime
import java.util.UUID


interface JpaReadingRecordRepository : JpaRepository<ReadingRecordEntity, UUID>, JpaReadingRecordQuerydslRepository {

    fun findAllByUserBookId(userBookId: UUID): List<ReadingRecordEntity>

    fun deleteAllByUserBookId(userBookId: UUID)

    fun findAllByUserBookId(userBookId: UUID, pageable: Pageable): Page<ReadingRecordEntity>

    fun findAllByUserBookIdIn(userBookIds: List<UUID>): List<ReadingRecordEntity>

    fun countByUserBookId(userBookId: UUID): Long

    fun findByUserBookIdInAndCreatedAtAfter(userBookIds: List<UUID>, createdAt: LocalDateTime): List<ReadingRecordEntity>
}
