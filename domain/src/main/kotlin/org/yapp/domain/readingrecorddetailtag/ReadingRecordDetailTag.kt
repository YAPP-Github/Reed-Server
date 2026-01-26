package org.yapp.domain.readingrecorddetailtag

import org.yapp.domain.detailtag.DetailTag
import org.yapp.domain.readingrecord.ReadingRecord
import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.*

data class ReadingRecordDetailTag private constructor(
    val id: Id,
    val readingRecordId: ReadingRecord.Id,
    val detailTagId: DetailTag.Id,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null
) {
    companion object {
        fun create(
            readingRecordId: UUID,
            detailTagId: UUID
        ): ReadingRecordDetailTag {
            return ReadingRecordDetailTag(
                id = Id.newInstance(UuidGenerator.create()),
                readingRecordId = ReadingRecord.Id.newInstance(readingRecordId),
                detailTagId = DetailTag.Id.newInstance(detailTagId)
            )
        }

        fun reconstruct(
            id: Id,
            readingRecordId: ReadingRecord.Id,
            detailTagId: DetailTag.Id,
            createdAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null,
            deletedAt: LocalDateTime? = null
        ): ReadingRecordDetailTag {
            return ReadingRecordDetailTag(
                id = id,
                readingRecordId = readingRecordId,
                detailTagId = detailTagId,
                createdAt = createdAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt
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

