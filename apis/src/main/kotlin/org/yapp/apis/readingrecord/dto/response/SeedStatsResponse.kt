package org.yapp.apis.readingrecord.dto.response

import org.yapp.domain.readingrecordtag.vo.TagStatsVO
import org.yapp.globalutils.tag.GeneralEmotionTagCategory

data class SeedStatsResponse private constructor(
    val categories: List<SeedCategoryStats>
) {
    data class SeedCategoryStats private constructor(
        val name: String,
        val count: Int
    ) {
        companion object {
            fun of(name: String, count: Int): SeedCategoryStats {
                return SeedCategoryStats(name, count)
            }
        }
    }

    companion object {
        fun from(tagStatsVO: TagStatsVO): SeedStatsResponse {
            val categories = GeneralEmotionTagCategory.entries.map { category ->
                SeedCategoryStats.of(
                    name = category.displayName,
                    count = tagStatsVO.categoryStats.getOrDefault(category, 0)
                )
            }
            return SeedStatsResponse(categories)
        }
    }
}
