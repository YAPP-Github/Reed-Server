package org.yapp.apis.readingrecord.service

import org.springframework.stereotype.Service
import org.yapp.apis.readingrecord.dto.response.SeedStatsResponse
import org.yapp.domain.readingrecordtag.ReadingRecordTagDomainService
import org.yapp.globalutils.tag.GeneralEmotionTagCategory
import java.util.*

@Service
class ReadingRecordTagService(
    private val readingRecordTagDomainService: ReadingRecordTagDomainService
) {
    fun getSeedStatsByUserIdAndUserBookId(userId: UUID, userBookId: UUID): SeedStatsResponse {
        val tagStatsVO = readingRecordTagDomainService.countTagsByUserIdAndUserBookIdAndCategories(
            userId = userId,
            userBookId = userBookId,
            categories = GeneralEmotionTagCategory.entries.map { it.displayName }
        )

        return SeedStatsResponse.from(tagStatsVO)
    }
}
