package org.yapp.domain.readingrecordtag

import org.yapp.domain.readingrecordtag.vo.TagStatsVO
import org.yapp.globalutils.annotation.DomainService
import java.util.*

@DomainService
class ReadingRecordTagDomainService(
    private val readingRecordTagRepository: ReadingRecordTagRepository
) {
    fun countTagsByUserIdAndUserBookIdAndCategories(
        userId: UUID,
        userBookId: UUID,
        categories: List<String>
    ): TagStatsVO {
        val categoryStats = readingRecordTagRepository.countTagsByUserIdAndUserBookIdAndCategories(userId, userBookId, categories)
        return TagStatsVO.newInstance(categoryStats)
    }
}
