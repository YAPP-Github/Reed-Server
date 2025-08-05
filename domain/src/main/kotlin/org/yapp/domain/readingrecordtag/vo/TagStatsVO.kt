package org.yapp.domain.readingrecordtag.vo

import org.yapp.globalutils.tag.GeneralEmotionTagCategory
import java.util.EnumMap

data class TagStatsVO private constructor(
    val categoryStats: EnumMap<GeneralEmotionTagCategory, Int>
) {
    init {
        categoryStats.values.forEach { count ->
            require(count >= 0) { "태그 개수는 0 이상이어야 합니다." }
        }
    }

    companion object {
        fun newInstance(categoryStats: Map<String, Int>): TagStatsVO {
            val enumStats = EnumMap<GeneralEmotionTagCategory, Int>(GeneralEmotionTagCategory::class.java)

            categoryStats.forEach { (displayName, count) ->
                GeneralEmotionTagCategory.fromDisplayName(displayName)?.let { enumCategory ->
                    enumStats[enumCategory] = count
                }
            }

            return TagStatsVO(enumStats)
        }
    }
}
