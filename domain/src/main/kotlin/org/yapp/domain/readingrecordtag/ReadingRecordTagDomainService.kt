package org.yapp.domain.readingrecordtag

import org.yapp.domain.readingrecordtag.vo.TagStatsVO
import org.yapp.globalutils.annotation.DomainService
import java.util.*

@DomainService
class ReadingRecordTagDomainService(
    private val readingRecordTagRepository: ReadingRecordTagRepository
) {
    fun countTagsByUserIdAndCategories(userId: UUID, categories: List<String>): TagStatsVO {
        val categoryStats = readingRecordTagRepository.countTagsByUserIdAndCategories(userId, categories)
        return TagStatsVO.newInstance(categoryStats)
    }
}
