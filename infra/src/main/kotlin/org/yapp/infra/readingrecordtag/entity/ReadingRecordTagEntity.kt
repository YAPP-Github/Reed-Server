package org.yapp.infra.readingrecordtag.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.yapp.domain.readingrecordtag.ReadingRecordTag
import org.yapp.infra.common.BaseTimeEntity
import java.sql.Types
import java.util.UUID

@Entity
@Table(name = "reading_record_tags")
@SQLDelete(sql = "UPDATE reading_record_tags SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class ReadingRecordTagEntity(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    val id: UUID,

    @Column(name = "reading_record_id", nullable = false, length = 36)
    @JdbcTypeCode(Types.VARCHAR)
    val readingRecordId: UUID,

    @Column(name = "tag_id", nullable = false, length = 36)
    @JdbcTypeCode(Types.VARCHAR)
    val tagId: UUID
) : BaseTimeEntity() {

    fun toDomain(): ReadingRecordTag {
        return ReadingRecordTag.reconstruct(
            id = ReadingRecordTag.Id.newInstance(this.id),
            readingRecordId = org.yapp.domain.readingrecord.ReadingRecord.Id.newInstance(this.readingRecordId),
            tagId = org.yapp.domain.tag.Tag.Id.newInstance(this.tagId),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deletedAt = this.deletedAt
        )
    }

    companion object {
        fun fromDomain(readingRecordTag: ReadingRecordTag): ReadingRecordTagEntity {
            return ReadingRecordTagEntity(
                id = readingRecordTag.id.value,
                readingRecordId = readingRecordTag.readingRecordId.value,
                tagId = readingRecordTag.tagId.value
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ReadingRecordTagEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}