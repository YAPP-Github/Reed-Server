package org.yapp.infra.readingrecordtag.repository.impl

import org.springframework.stereotype.Repository
import org.yapp.domain.readingrecordtag.ReadingRecordTag
import org.yapp.domain.readingrecordtag.ReadingRecordTagRepository
import org.yapp.infra.readingrecordtag.entity.ReadingRecordTagEntity
import org.yapp.infra.readingrecordtag.repository.JpaReadingRecordTagRepository
import java.util.*

@Repository
class ReadingRecordTagRepositoryImpl(
    private val jpaReadingRecordTagRepository: JpaReadingRecordTagRepository
) : ReadingRecordTagRepository {
    override fun saveAll(readingRecordTags: List<ReadingRecordTag>): List<ReadingRecordTag> {
        val entities = readingRecordTags.map { ReadingRecordTagEntity.fromDomain(it) }
        return jpaReadingRecordTagRepository.saveAll(entities).map { it.toDomain() }
    }

    override fun findByReadingRecordId(readingRecordId: UUID): List<ReadingRecordTag> {
        return jpaReadingRecordTagRepository.findByReadingRecordId(readingRecordId).map { it.toDomain() }
    }

    override fun countTagsByUserIdAndUserBookIdAndCategories(userId: UUID, userBookId: UUID, categories: List<String>): Map<String, Int> {
        return jpaReadingRecordTagRepository.countTagsByUserIdAndUserBookIdAndCategories(userId, userBookId, categories)
    }
}
