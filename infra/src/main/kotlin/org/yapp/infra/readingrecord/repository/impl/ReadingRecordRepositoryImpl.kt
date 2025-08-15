package org.yapp.infra.readingrecord.repository.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.yapp.domain.readingrecord.ReadingRecord
import org.yapp.domain.readingrecord.ReadingRecordRepository
import org.yapp.domain.readingrecord.ReadingRecordSortType
import org.yapp.infra.readingrecord.entity.ReadingRecordEntity
import org.yapp.infra.readingrecord.repository.JpaReadingRecordRepository
import java.util.*

@Repository
class ReadingRecordRepositoryImpl(
    private val jpaReadingRecordRepository: JpaReadingRecordRepository
) : ReadingRecordRepository {

    override fun save(readingRecord: ReadingRecord): ReadingRecord {
        val savedEntity = jpaReadingRecordRepository.saveAndFlush(ReadingRecordEntity.fromDomain(readingRecord))
        return savedEntity.toDomain()
    }

    override fun deleteAllByUserBookId(userBookId: UUID) {
        jpaReadingRecordRepository.deleteAllByUserBookId(userBookId)
    }

    override fun findById(id: UUID): ReadingRecord? {
        return jpaReadingRecordRepository.findByIdOrNull(id)?.toDomain()
    }

    override fun findAllByUserBookId(userBookId: UUID): List<ReadingRecord> {
        val entities = jpaReadingRecordRepository.findAllByUserBookId(userBookId)
        return entities.map { it.toDomain() }
    }

    override fun findAllByUserBookId(userBookId: UUID, pageable: Pageable): Page<ReadingRecord> {
        val page = jpaReadingRecordRepository.findAllByUserBookId(userBookId, pageable)
        return page.map { it.toDomain() }
    }

    override fun findAllByUserBookIdIn(userBookIds: List<UUID>): List<ReadingRecord> {
        val entities = jpaReadingRecordRepository.findAllByUserBookIdIn(userBookIds)
        return entities.map { it.toDomain() }
    }

    override fun countByUserBookId(userBookId: UUID): Long {
        return jpaReadingRecordRepository.countByUserBookId(userBookId)
    }

    override fun findReadingRecordsByDynamicCondition(
        userBookId: UUID,
        sort: ReadingRecordSortType?,
        pageable: Pageable
    ): Page<ReadingRecord> {
        val page = jpaReadingRecordRepository.findReadingRecordsByDynamicCondition(userBookId, sort, pageable)
        return page.map { it.toDomain() }
    }

    override fun deleteById(id: UUID) {
        jpaReadingRecordRepository.deleteById(id)
    }
}
