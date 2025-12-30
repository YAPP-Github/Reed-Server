package org.yapp.apis.readingrecord.usecase

import org.springframework.data.domain.Pageable

import org.yapp.apis.book.service.UserBookService
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.request.UpdateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.response.ReadingRecordPageResponse // Added import
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponse
import org.yapp.apis.readingrecord.dto.response.SeedStatsResponse
import org.yapp.apis.readingrecord.service.ReadingRecordService
import org.yapp.apis.readingrecord.service.ReadingRecordTagService
import org.yapp.apis.user.service.UserService
import org.yapp.domain.readingrecord.ReadingRecordSortType
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
class ReadingRecordUseCase(
    private val readingRecordService: ReadingRecordService,
    private val readingRecordTagService: ReadingRecordTagService,
    private val userService: UserService,
    private val userBookService: UserBookService,
) {
    fun createReadingRecord(
        userId: UUID,
        userBookId: UUID,
        request: CreateReadingRecordRequest
    ): ReadingRecordResponse {
        userService.validateUserExists(userId)
        userBookService.validateUserBookExists(userBookId, userId)

        return readingRecordService.createReadingRecord(
            userId = userId,
            userBookId = userBookId,
            request = request
        )
    }

    fun getReadingRecordDetail(
        userId: UUID,
        readingRecordId: UUID
    ): ReadingRecordResponse {
        userService.validateUserExists(userId)
        val userBookId = readingRecordService.getUserBookIdByReadingRecordId(readingRecordId)
        userBookService.validateUserBookExists(userBookId, userId)

        return readingRecordService.getReadingRecordDetail(
            userId = userId,
            readingRecordId = readingRecordId
        )
    }

    fun getReadingRecordsByUserBookId(
        userId: UUID,
        userBookId: UUID,
        sort: ReadingRecordSortType?,
        pageable: Pageable
    ): ReadingRecordPageResponse {
        userService.validateUserExists(userId)
        userBookService.validateUserBookExists(userBookId, userId)

        val page = readingRecordService.getReadingRecordsByDynamicCondition( // Stored in a variable
            userBookId = userBookId,
            sort = sort,
            pageable = pageable
        )
        return ReadingRecordPageResponse.from(page) // Converted to new DTO
    }

    fun getSeedStats(
        userId: UUID,
        userBookId: UUID
    ): SeedStatsResponse {
        userService.validateUserExists(userId)
        userBookService.validateUserBookExists(userBookId, userId)
        return readingRecordTagService.getSeedStatsByUserIdAndUserBookId(userId, userBookId)
    }


    fun updateReadingRecord(
        userId: UUID,
        readingRecordId: UUID,
        request: UpdateReadingRecordRequest
    ): ReadingRecordResponse {
        userService.validateUserExists(userId)
        val userBookId = readingRecordService.getUserBookIdByReadingRecordId(readingRecordId)
        userBookService.validateUserBookExists(userBookId, userId)

        return readingRecordService.updateReadingRecord(
            userId = userId,
            readingRecordId = readingRecordId,
            request = request
        )
    }

    fun deleteReadingRecord(
        userId: UUID,
        readingRecordId: UUID
    ) {
        userService.validateUserExists(userId)
        val userBookId = readingRecordService.getUserBookIdByReadingRecordId(readingRecordId)
        userBookService.validateUserBookExists(userBookId, userId)
        readingRecordService.deleteReadingRecord(readingRecordId)
    }
}
