package org.yapp.infra.readingrecordtag.repository

import java.util.*

interface JpaReadingRecordTagQuerydslRepository {
    fun countTagsByUserIdAndCategories(userId: UUID, categories: List<String>): Map<String, Int>
} 