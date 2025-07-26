package org.yapp.infra.readingrecordtag.repository

import org.springframework.stereotype.Repository
import org.yapp.domain.readingrecordtag.ReadingRecordTag
import org.yapp.domain.readingrecordtag.ReadingRecordTagRepository
import org.yapp.infra.readingrecordtag.entity.ReadingRecordTagEntity

@Repository
class ReadingRecordTagRepositoryImpl(
    private val jpaReadingRecordTagRepository: JpaReadingRecordTagRepository
) : ReadingRecordTagRepository {
    override fun saveAll(readingRecordTags: List<ReadingRecordTag>): List<ReadingRecordTag> {
        val entities = readingRecordTags.map { ReadingRecordTagEntity.fromDomain(it) }
        return jpaReadingRecordTagRepository.saveAll(entities).map { it.toDomain() }
    }

    override fun findByReadingRecordId(readingRecordId: java.util.UUID): List<ReadingRecordTag> {
        return jpaReadingRecordTagRepository.findByReadingRecordId(readingRecordId).map { it.toDomain() }
    }
}