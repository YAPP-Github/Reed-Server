package org.yapp.infra.detailtag.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.yapp.domain.detailtag.DetailTag
import org.yapp.domain.readingrecord.PrimaryEmotion
import org.yapp.infra.common.BaseTimeEntity
import java.sql.Types
import java.util.*

@Entity
@Table(
    name = "detail_tags",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_detail_tags_emotion_name",
            columnNames = ["primary_emotion", "name"]
        )
    ]
)
class DetailTagEntity(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    val id: UUID,

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_emotion", nullable = false, length = 20)
    val primaryEmotion: PrimaryEmotion,

    name: String,
    displayOrder: Int = 0
) : BaseTimeEntity() {

    @Column(nullable = false, length = 20)
    var name: String = name
        protected set

    @Column(name = "display_order", nullable = false)
    var displayOrder: Int = displayOrder
        protected set

    fun toDomain(): DetailTag {
        return DetailTag.reconstruct(
            id = DetailTag.Id.newInstance(this.id),
            primaryEmotion = this.primaryEmotion,
            name = this.name,
            displayOrder = this.displayOrder,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    companion object {
        fun fromDomain(detailTag: DetailTag): DetailTagEntity {
            return DetailTagEntity(
                id = detailTag.id.value,
                primaryEmotion = detailTag.primaryEmotion,
                name = detailTag.name,
                displayOrder = detailTag.displayOrder
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DetailTagEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

