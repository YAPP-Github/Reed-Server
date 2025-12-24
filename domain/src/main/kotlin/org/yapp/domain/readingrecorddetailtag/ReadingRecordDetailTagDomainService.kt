package org.yapp.domain.readingrecorddetailtag

import org.yapp.globalutils.annotation.DomainService
import java.util.*

@DomainService
class ReadingRecordDetailTagDomainService(
    private val readingRecordDetailTagRepository: ReadingRecordDetailTagRepository
) {
    fun findByReadingRecordId(readingRecordId: UUID): List<ReadingRecordDetailTag> {
        return readingRecordDetailTagRepository.findByReadingRecordId(readingRecordId)
    }

    fun findByReadingRecordIdIn(readingRecordIds: List<UUID>): List<ReadingRecordDetailTag> {
        if (readingRecordIds.isEmpty()) return emptyList()
        return readingRecordDetailTagRepository.findByReadingRecordIdIn(readingRecordIds)
    }

    fun deleteAllByReadingRecordId(readingRecordId: UUID) {
        readingRecordDetailTagRepository.deleteAllByReadingRecordId(readingRecordId)
    }

    fun createAndSaveAll(readingRecordId: UUID, detailTagIds: List<UUID>): List<ReadingRecordDetailTag> {
        if (detailTagIds.isEmpty()) return emptyList()
        val readingRecordDetailTags = detailTagIds.map { detailTagId ->
            ReadingRecordDetailTag.create(
                readingRecordId = readingRecordId,
                detailTagId = detailTagId
            )
        }
        return readingRecordDetailTagRepository.saveAll(readingRecordDetailTags)
    }
}

