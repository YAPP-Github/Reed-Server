package org.yapp.infra.detailtag.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yapp.domain.readingrecord.PrimaryEmotion
import org.yapp.infra.detailtag.entity.DetailTagEntity
import java.util.*

interface JpaDetailTagRepository : JpaRepository<DetailTagEntity, UUID> {
    fun findByPrimaryEmotionOrderByDisplayOrderAsc(primaryEmotion: PrimaryEmotion): List<DetailTagEntity>
    fun findAllByOrderByPrimaryEmotionAscDisplayOrderAsc(): List<DetailTagEntity>
}

