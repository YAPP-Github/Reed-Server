package org.yapp.infra.readingrecorddetailtag.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.yapp.domain.detailtag.DetailTag
import org.yapp.domain.readingrecord.ReadingRecord
import org.yapp.domain.readingrecorddetailtag.ReadingRecordDetailTag
import org.yapp.infra.common.BaseTimeEntity
import java.sql.Types
import java.util.*

@Entity
@Table(
    name = "reading_record_detail_tags",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_record_detail_tag",
            columnNames = ["reading_record_id", "detail_tag_id"]
        )
    ],
    indexes = [
        Index(name = "idx_rrdt_reading_record_id", columnList = "reading_record_id"),
        Index(name = "idx_rrdt_detail_tag_id", columnList = "detail_tag_id")
    ]
)
class ReadingRecordDetailTagEntity(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    val id: UUID,

    @Column(name = "reading_record_id", nullable = false, length = 36)
    @JdbcTypeCode(Types.VARCHAR)
    val readingRecordId: UUID,

    @Column(name = "detail_tag_id", nullable = false, length = 36)
    @JdbcTypeCode(Types.VARCHAR)
    val detailTagId: UUID
) : BaseTimeEntity() {

    fun toDomain(): ReadingRecordDetailTag {
        return ReadingRecordDetailTag.reconstruct(
            id = ReadingRecordDetailTag.Id.newInstance(this.id),
            readingRecordId = ReadingRecord.Id.newInstance(this.readingRecordId),
            detailTagId = DetailTag.Id.newInstance(this.detailTagId),
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deletedAt = this.deletedAt
        )
    }

    companion object {
        fun fromDomain(readingRecordDetailTag: ReadingRecordDetailTag): ReadingRecordDetailTagEntity {
            return ReadingRecordDetailTagEntity(
                id = readingRecordDetailTag.id.value,
                readingRecordId = readingRecordDetailTag.readingRecordId.value,
                detailTagId = readingRecordDetailTag.detailTagId.value
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ReadingRecordDetailTagEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
