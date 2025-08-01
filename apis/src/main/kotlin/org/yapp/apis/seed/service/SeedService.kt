package org.yapp.apis.seed.service

import org.springframework.stereotype.Service
import org.yapp.apis.seed.dto.response.SeedStatsResponse
import org.yapp.domain.readingrecordtag.ReadingRecordTagDomainService
import org.yapp.globalutils.tag.GeneralEmotionTagCategory
import java.util.*

@Service
class SeedService(
    private val readingRecordTagDomainService: ReadingRecordTagDomainService
) {
    fun getSeedStatsByUserId(userId: UUID): SeedStatsResponse {
        val tagStatsVO = readingRecordTagDomainService.countTagsByUserIdAndCategories(
            userId = userId,
            categories = GeneralEmotionTagCategory.entries.map { it.displayName }
        )

        return SeedStatsResponse.from(tagStatsVO)
    }
} 
