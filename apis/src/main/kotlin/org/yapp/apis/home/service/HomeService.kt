package org.yapp.apis.home.service

import org.yapp.apis.home.dto.response.UserHomeResponse
import org.yapp.domain.userbook.UserBookDomainService
import org.yapp.domain.userbook.vo.HomeBookVO
import org.yapp.globalutils.annotation.ApplicationService
import java.util.*

@ApplicationService
class HomeService(
    private val userBookDomainService: UserBookDomainService
) {
    fun getUserHomeData(userId: UUID, limit: Int): UserHomeResponse {
        val selectedBooks = selectBooksForHome(userId, limit)
        return UserHomeResponse.from(selectedBooks)
    }

    private fun selectBooksForHome(userId: UUID, limit: Int): List<HomeBookVO> {
        val booksWithRecords = userBookDomainService.findBooksWithRecordsOrderByLatest(userId)

        if (booksWithRecords.size >= limit) {
            return booksWithRecords.take(limit)
        }

        val neededCount = limit - booksWithRecords.size
        val excludedBookIds = booksWithRecords.map { it.id.value }.toSet()

        val booksWithoutRecords = userBookDomainService.findBooksWithoutRecordsByStatusPriority(
            userId,
            neededCount,
            excludedBookIds
        )

        return booksWithRecords + booksWithoutRecords
    }
} 
