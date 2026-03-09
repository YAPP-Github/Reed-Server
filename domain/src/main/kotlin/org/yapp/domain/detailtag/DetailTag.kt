package org.yapp.domain.detailtag

import org.yapp.domain.readingrecord.PrimaryEmotion
import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.*

data class DetailTag private constructor(
    val id: Id,
    val primaryEmotion: PrimaryEmotion,
    val name: String,
    val displayOrder: Int,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun create(
            primaryEmotion: PrimaryEmotion,
            name: String,
            displayOrder: Int
        ): DetailTag {
            require(name.isNotBlank()) { "Detail tag name cannot be blank" }
            require(displayOrder >= 0) { "Display order must be non-negative" }

            return DetailTag(
                id = Id.newInstance(UuidGenerator.create()),
                primaryEmotion = primaryEmotion,
                name = name,
                displayOrder = displayOrder
            )
        }

        fun reconstruct(
            id: Id,
            primaryEmotion: PrimaryEmotion,
            name: String,
            displayOrder: Int,
            createdAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null
        ): DetailTag {
            return DetailTag(
                id = id,
                primaryEmotion = primaryEmotion,
                name = name,
                displayOrder = displayOrder,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }

    @JvmInline
    value class Id(val value: UUID) {
        companion object {
            fun newInstance(value: UUID) = Id(value)
        }
    }
}
