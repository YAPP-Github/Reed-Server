package org.yapp.apis.readingrecord.usecase

import org.springframework.data.domain.Pageable
import org.yapp.apis.book.service.UserBookService
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequestV2
import org.yapp.apis.readingrecord.dto.request.UpdateReadingRecordRequestV2
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponseV2
import org.yapp.apis.readingrecord.dto.response.ReadingRecordsWithPrimaryEmotionResponse
import org.yapp.apis.readingrecord.dto.response.SeedStatsResponseV2
import org.yapp.apis.readingrecord.service.ReadingRecordServiceV2
import org.yapp.apis.user.service.UserService
import org.yapp.domain.readingrecord.ReadingRecordSortType
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
class ReadingRecordUseCaseV2(
    private val readingRecordServiceV2: ReadingRecordServiceV2,
    private val userService: UserService,
    private val userBookService: UserBookService,
) {
    fun createReadingRecord(
        userId: UUID,
        userBookId: UUID,
        request: CreateReadingRecordRequestV2
    ): ReadingRecordResponseV2 {
        userService.validateUserExists(userId)
        userBookService.validateUserBookExists(userBookId, userId)

        return readingRecordServiceV2.createReadingRecord(
            userId = userId,
            userBookId = userBookId,
            request = request
        )
    }

    fun getReadingRecordDetail(
        userId: UUID,
        readingRecordId: UUID
    ): ReadingRecordResponseV2 {
        userService.validateUserExists(userId)
        val userBookId = readingRecordServiceV2.getUserBookIdByReadingRecordId(readingRecordId)
        userBookService.validateUserBookExists(userBookId, userId)

        return readingRecordServiceV2.getReadingRecordDetail(
            readingRecordId = readingRecordId
        )
    }

    fun getReadingRecordsByUserBookId(
        userId: UUID,
        userBookId: UUID,
        sort: ReadingRecordSortType?,
        pageable: Pageable
    ): ReadingRecordsWithPrimaryEmotionResponse {
        userService.validateUserExists(userId)
        userBookService.validateUserBookExists(userBookId, userId)

        return readingRecordServiceV2.getReadingRecordsByDynamicCondition(
            userBookId = userBookId,
            sort = sort,
            pageable = pageable
        )
    }
    
    fun updateReadingRecord(
        userId: UUID,
        readingRecordId: UUID,
        request: UpdateReadingRecordRequestV2
    ): ReadingRecordResponseV2 {
        userService.validateUserExists(userId)
        val userBookId = readingRecordServiceV2.getUserBookIdByReadingRecordId(readingRecordId)
        userBookService.validateUserBookExists(userBookId, userId)

        return readingRecordServiceV2.updateReadingRecord(
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
        val userBookId = readingRecordServiceV2.getUserBookIdByReadingRecordId(readingRecordId)
        userBookService.validateUserBookExists(userBookId, userId)
        readingRecordServiceV2.deleteReadingRecord(readingRecordId)
    }

    fun getSeedStats(
        userId: UUID,
        userBookId: UUID
    ): SeedStatsResponseV2 {
        userService.validateUserExists(userId)
        userBookService.validateUserBookExists(userBookId, userId)

        return readingRecordServiceV2.getSeedStats(userBookId)
    }
}
