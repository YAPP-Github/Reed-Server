package org.yapp.infra.readingrecorddetailtag.repository

import org.springframework.stereotype.Repository
import org.yapp.domain.readingrecorddetailtag.ReadingRecordDetailTag
import org.yapp.domain.readingrecorddetailtag.ReadingRecordDetailTagRepository
import org.yapp.infra.readingrecorddetailtag.entity.ReadingRecordDetailTagEntity
import java.util.*

@Repository
class ReadingRecordDetailTagRepositoryImpl(
    private val jpaReadingRecordDetailTagRepository: JpaReadingRecordDetailTagRepository
) : ReadingRecordDetailTagRepository {

    override fun findByReadingRecordId(readingRecordId: UUID): List<ReadingRecordDetailTag> {
        return jpaReadingRecordDetailTagRepository.findByReadingRecordId(readingRecordId)
            .map { it.toDomain() }
    }

    override fun findByReadingRecordIdIn(readingRecordIds: List<UUID>): List<ReadingRecordDetailTag> {
        if (readingRecordIds.isEmpty()) return emptyList()
        return jpaReadingRecordDetailTagRepository.findByReadingRecordIdIn(readingRecordIds)
            .map { it.toDomain() }
    }

    override fun save(readingRecordDetailTag: ReadingRecordDetailTag): ReadingRecordDetailTag {
        val entity = ReadingRecordDetailTagEntity.fromDomain(readingRecordDetailTag)
        return jpaReadingRecordDetailTagRepository.save(entity).toDomain()
    }

    override fun saveAll(readingRecordDetailTags: List<ReadingRecordDetailTag>): List<ReadingRecordDetailTag> {
        if (readingRecordDetailTags.isEmpty()) return emptyList()
        val entities = readingRecordDetailTags.map { ReadingRecordDetailTagEntity.fromDomain(it) }
        return jpaReadingRecordDetailTagRepository.saveAll(entities).map { it.toDomain() }
    }

    override fun deleteAllByReadingRecordId(readingRecordId: UUID) {
        jpaReadingRecordDetailTagRepository.deleteAllByReadingRecordId(readingRecordId)
    }
}
