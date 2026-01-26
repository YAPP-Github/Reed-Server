package org.yapp.domain.detailtag

import org.yapp.domain.readingrecord.PrimaryEmotion
import java.util.*

interface DetailTagRepository {
    fun findById(id: UUID): DetailTag?
    fun findAllById(ids: List<UUID>): List<DetailTag>
    fun findByPrimaryEmotion(primaryEmotion: PrimaryEmotion): List<DetailTag>
    fun findAll(): List<DetailTag>
    fun save(detailTag: DetailTag): DetailTag
    fun saveAll(detailTags: List<DetailTag>): List<DetailTag>
}
