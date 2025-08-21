package org.yapp.infra.tag.repository

import org.springframework.stereotype.Repository
import org.yapp.domain.tag.Tag
import org.yapp.domain.tag.TagRepository
import org.yapp.infra.tag.entity.TagEntity
import java.util.UUID

@Repository
class TagRepositoryImpl(
    private val jpaTagRepository: JpaTagRepository
) : TagRepository {
    override fun findByName(name: String): Tag? {
        return jpaTagRepository.findByName(name)?.toDomain()
    }

    override fun save(tag: Tag): Tag {
        return jpaTagRepository.save(TagEntity.fromDomain(tag)).toDomain()
    }

    override fun findByNames(names: List<String>): List<Tag> {
        return jpaTagRepository.findByNameIn(names).map { it.toDomain() }
    }

    override fun findByIds(ids: List<UUID>): List<Tag> {
        return jpaTagRepository.findByIdIn(ids).map { it.toDomain() }
    }
}