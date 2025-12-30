package org.yapp.infra.detailtag.repository

import org.springframework.stereotype.Repository
import org.yapp.domain.detailtag.DetailTag
import org.yapp.domain.detailtag.DetailTagRepository
import org.yapp.domain.readingrecord.PrimaryEmotion
import org.yapp.infra.detailtag.entity.DetailTagEntity
import java.util.*

@Repository
class DetailTagRepositoryImpl(
    private val jpaDetailTagRepository: JpaDetailTagRepository
) : DetailTagRepository {

    override fun findById(id: UUID): DetailTag? {
        return jpaDetailTagRepository.findById(id)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findAllById(ids: List<UUID>): List<DetailTag> {
        return jpaDetailTagRepository.findAllById(ids)
            .map { it.toDomain() }
    }

    override fun findByPrimaryEmotion(primaryEmotion: PrimaryEmotion): List<DetailTag> {
        return jpaDetailTagRepository.findByPrimaryEmotionOrderByDisplayOrderAsc(primaryEmotion)
            .map { it.toDomain() }
    }

    override fun findAll(): List<DetailTag> {
        return jpaDetailTagRepository.findAllByOrderByPrimaryEmotionAscDisplayOrderAsc()
            .map { it.toDomain() }
    }

    override fun save(detailTag: DetailTag): DetailTag {
        val entity = DetailTagEntity.fromDomain(detailTag)
        return jpaDetailTagRepository.save(entity).toDomain()
    }

    override fun saveAll(detailTags: List<DetailTag>): List<DetailTag> {
        val entities = detailTags.map { DetailTagEntity.fromDomain(it) }
        return jpaDetailTagRepository.saveAll(entities).map { it.toDomain() }
    }
}
