package org.yapp.globalutils.tag

enum class GeneralEmotionTagCategory(
    val displayName: String
) {
    WARMTH("따뜻함"),
    JOY("즐거움"),
    SADNESS("슬픔"),
    REALIZATION("깨달음");

    companion object {
        private val BY_DISPLAY_NAME = entries.associateBy { it.displayName }

        fun fromDisplayName(displayName: String): GeneralEmotionTagCategory? {
            return BY_DISPLAY_NAME[displayName]
        }
    }
}
