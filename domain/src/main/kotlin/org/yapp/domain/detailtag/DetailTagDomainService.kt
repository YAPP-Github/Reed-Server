package org.yapp.domain.detailtag

import org.yapp.domain.readingrecord.PrimaryEmotion
import org.yapp.globalutils.annotation.DomainService
import java.util.*

@DomainService
class DetailTagDomainService(
    private val detailTagRepository: DetailTagRepository
) {
    fun findById(id: UUID): DetailTag? {
        return detailTagRepository.findById(id)
    }

    fun findAllById(ids: List<UUID>): List<DetailTag> {
        if (ids.isEmpty()) return emptyList()
        return detailTagRepository.findAllById(ids)
    }

    fun findByPrimaryEmotion(primaryEmotion: PrimaryEmotion): List<DetailTag> {
        return detailTagRepository.findByPrimaryEmotion(primaryEmotion)
    }

    fun findAll(): List<DetailTag> {
        return detailTagRepository.findAll()
    }
}
