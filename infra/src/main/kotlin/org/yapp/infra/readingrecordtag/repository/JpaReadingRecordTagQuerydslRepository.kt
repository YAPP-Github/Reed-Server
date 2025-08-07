package org.yapp.infra.readingrecordtag.repository

import java.util.*

interface JpaReadingRecordTagQuerydslRepository {
    fun countTagsByUserIdAndUserBookIdAndCategories(
        userId: UUID,
        userBookId: UUID,
        categories: List<String>
    ): Map<String, Int>
} 
