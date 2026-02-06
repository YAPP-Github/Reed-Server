package org.yapp.infra.readingrecord.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.yapp.domain.readingrecord.ReadingRecordSortType
import org.yapp.domain.readingrecord.PrimaryEmotion
import org.yapp.infra.readingrecord.entity.ReadingRecordEntity
import java.util.UUID

interface JpaReadingRecordQuerydslRepository {

    fun findReadingRecordsByDynamicCondition(
        userBookId: UUID,
        sort: ReadingRecordSortType?,
        pageable: Pageable
    ): Page<ReadingRecordEntity>

    fun findMostFrequentPrimaryEmotion(userBookId: UUID): PrimaryEmotion?

    fun countPrimaryEmotionsByUserBookId(userBookId: UUID): Map<PrimaryEmotion, Int>
}
