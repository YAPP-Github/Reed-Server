package org.yapp.domain.tag

import java.util.UUID

interface TagRepository {
    fun findByName(name: String): Tag?
    fun save(tag: Tag): Tag
    fun findByNames(names: List<String>): List<Tag>
    fun findByIds(ids: List<UUID>): List<Tag>
}
