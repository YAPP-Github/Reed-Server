package org.yapp.apis.readingrecord.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.readingrecordtag.vo.TagStatsVO
import org.yapp.globalutils.tag.GeneralEmotionTagCategory

@Schema(
    name = "SeedStatsResponse",
    description = "감정 태그 카테고리별 씨앗 통계 응답"
)
data class SeedStatsResponse private constructor(

    @field:Schema(
        description = "각 감정 카테고리의 통계 리스트",
        example = "[{\"name\":\"따뜻함\",\"count\":3},{\"name\":\"즐거움\",\"count\":1}]"
    )
    val categories: List<SeedCategoryStats>
) {

    @Schema(
        name = "SeedCategoryStats",
        description = "단일 감정 카테고리의 통계 정보"
    )
    data class SeedCategoryStats private constructor(

        @field:Schema(
            description = "감정 카테고리 이름",
            example = "따뜻함"
        )
        val name: String,

        @field:Schema(
            description = "해당 감정 카테고리의 씨앗 개수",
            example = "3",
            minimum = "0"
        )
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
