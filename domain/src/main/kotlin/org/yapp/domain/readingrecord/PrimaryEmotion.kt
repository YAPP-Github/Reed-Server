package org.yapp.domain.readingrecord

enum class PrimaryEmotion(val displayName: String) {
    WARMTH("따뜻함"),
    JOY("즐거움"),
    SADNESS("슬픔"),
    INSIGHT("깨달음"),
    OTHER("기타");

    companion object {
        fun fromDisplayName(name: String): PrimaryEmotion =
            entries.find { it.displayName == name } ?: OTHER

        fun fromCode(code: String): PrimaryEmotion =
            entries.find { it.name == code } ?: OTHER
    }
}
