package org.yapp.apis.readingrecord.usecase

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.book.service.UserBookService
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponse
import org.yapp.apis.readingrecord.dto.response.SeedStatsResponse
import org.yapp.apis.readingrecord.service.ReadingRecordService
import org.yapp.apis.readingrecord.service.ReadingRecordTagService
import org.yapp.apis.user.service.UserService
import org.yapp.domain.readingrecord.ReadingRecordSortType
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
@Transactional(readOnly = true)
class ReadingRecordUseCase(
    private val readingRecordService: ReadingRecordService,
    private val readingRecordTagService: ReadingRecordTagService,
    private val userService: UserService,
    private val userBookService: UserBookService,
) {
    @Transactional
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
    ): Page<ReadingRecordResponse> {
        userService.validateUserExists(userId)
        userBookService.validateUserBookExists(userBookId, userId)

        return readingRecordService.getReadingRecordsByDynamicCondition(
            userBookId = userBookId,
            sort = sort,
            pageable = pageable
        )
    }

    fun getSeedStats(
        userId: UUID,
        userBookId: UUID
    ): SeedStatsResponse {
        userService.validateUserExists(userId)
        return readingRecordTagService.getSeedStatsByUserIdAndUserBookId(userId, userBookId)
    }
}
