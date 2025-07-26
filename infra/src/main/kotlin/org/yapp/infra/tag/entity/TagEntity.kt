package org.yapp.infra.tag.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.yapp.domain.tag.Tag
import org.yapp.infra.common.BaseTimeEntity
import java.sql.Types
import java.util.UUID

@Entity
@Table(name = "tags")
@SQLDelete(sql = "UPDATE tags SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class TagEntity(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    val id: UUID,

    @Column(nullable = false, length = 10, unique = true)
    val name: String
) : BaseTimeEntity() {

    fun toDomain(): Tag {
        return Tag.reconstruct(
            id = Tag.Id.newInstance(this.id),
            name = this.name,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deletedAt = this.deletedAt
        )
    }

    companion object {
        fun fromDomain(tag: Tag): TagEntity {
            return TagEntity(
                id = tag.id.value,
                name = tag.name
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TagEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}