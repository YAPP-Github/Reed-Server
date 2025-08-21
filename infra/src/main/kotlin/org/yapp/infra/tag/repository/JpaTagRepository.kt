package org.yapp.infra.tag.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yapp.infra.tag.entity.TagEntity
import java.util.UUID

interface JpaTagRepository : JpaRepository<TagEntity, UUID> {
    fun findByName(name: String): TagEntity?
    fun findByNameIn(names: List<String>): List<TagEntity>
    fun findByIdIn(ids: List<UUID>): List<TagEntity>
}
