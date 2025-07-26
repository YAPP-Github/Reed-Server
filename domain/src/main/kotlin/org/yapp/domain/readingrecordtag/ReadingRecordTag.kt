package org.yapp.domain.readingrecordtag

import org.yapp.domain.readingrecord.ReadingRecord
import org.yapp.domain.tag.Tag
import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.UUID

data class ReadingRecordTag private constructor(
    val id: Id,
    val readingRecordId: ReadingRecord.Id,
    val tagId: Tag.Id,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) {
    companion object {
        fun create(readingRecordId: UUID, tagId: UUID): ReadingRecordTag {
            return ReadingRecordTag(
                id = Id.newInstance(UuidGenerator.create()),
                readingRecordId = ReadingRecord.Id.newInstance(readingRecordId),
                tagId = Tag.Id.newInstance(tagId)
            )
        }

        fun reconstruct(
            id: Id,
            readingRecordId: ReadingRecord.Id,
            tagId: Tag.Id,
            createdAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null,
            deletedAt: LocalDateTime? = null
        ): ReadingRecordTag {
            return ReadingRecordTag(
                id = id,
                readingRecordId = readingRecordId,
                tagId = tagId,
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