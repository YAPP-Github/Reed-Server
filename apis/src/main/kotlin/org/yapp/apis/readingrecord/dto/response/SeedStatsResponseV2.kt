package org.yapp.apis.readingrecord.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.readingrecord.PrimaryEmotion

@Schema(
    name = "SeedStatsResponseV2",
    description = "Seed statistics by emotion category (V2 - includes OTHER)"
)
data class SeedStatsResponseV2 private constructor(
    @field:Schema(description = "List of statistics for each emotion category (5 categories)")
    val categories: List<SeedCategoryStats>
) {
    @Schema(name = "SeedCategoryStats", description = "Statistics for a single emotion category")
    data class SeedCategoryStats private constructor(
        @field:Schema(description = "Emotion category name", example = "따뜻함")
        val name: String,

        @field:Schema(description = "Number of seeds for this emotion category", example = "3", minimum = "0")
        val count: Int
    ) {
        companion object {
            fun of(name: String, count: Int): SeedCategoryStats {
                return SeedCategoryStats(name, count)
            }
        }
    }

    companion object {
        fun from(primaryEmotionCounts: Map<PrimaryEmotion, Int>): SeedStatsResponseV2 {
            val categories = PrimaryEmotion.entries.map { emotion ->
                SeedCategoryStats.of(
                    name = emotion.displayName,
                    count = primaryEmotionCounts.getOrDefault(emotion, 0)
                )
            }
            return SeedStatsResponseV2(categories)
        }
    }
}
