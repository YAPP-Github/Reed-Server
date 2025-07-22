package org.yapp.domain.userbook.vo

import org.yapp.domain.userbook.BookStatus


data class UserBookStatusCountsVO private constructor(
    val beforeReadingCount: Long,
    val readingCount: Long,
    val completedCount: Long
) {
    init {
        require(beforeReadingCount >= 0) { "Before reading count cannot be negative" }
        require(readingCount >= 0) { "Reading count cannot be negative" }
        require(completedCount >= 0) { "Completed count cannot be negative" }
    }

    companion object {
        fun newInstance(
            beforeReadingCount: Long,
            readingCount: Long,
            completedCount: Long
        ): UserBookStatusCountsVO {
            return UserBookStatusCountsVO(
                beforeReadingCount = beforeReadingCount,
                readingCount = readingCount,
                completedCount = completedCount
            )
        }

        fun newInstance(statusCounts: Map<BookStatus, Long>): UserBookStatusCountsVO {
            return UserBookStatusCountsVO(
                beforeReadingCount = statusCounts[BookStatus.BEFORE_READING] ?: 0L,
                readingCount = statusCounts[BookStatus.READING] ?: 0L,
                completedCount = statusCounts[BookStatus.COMPLETED] ?: 0L
            )
        }
    }
}
