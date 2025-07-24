package org.yapp.apis.readingrecord.usecase

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.auth.service.UserAuthService
import org.yapp.apis.book.service.UserBookService
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponse
import org.yapp.apis.readingrecord.service.ReadingRecordService
import org.yapp.globalutils.annotation.UseCase
import java.util.UUID


@UseCase
@Transactional(readOnly = true)
class ReadingRecordUseCase(
    private val readingRecordService: ReadingRecordService,
    private val userAuthService: UserAuthService,
    private val userBookService: UserBookService
) {
    @Transactional
    fun createReadingRecord(
        userId: UUID,
        userBookId: UUID,
        request: CreateReadingRecordRequest
    ): ReadingRecordResponse {
        userAuthService.validateUserExists(userId)
        userBookService.validateUserBookExists(userId, userBookId)

        return readingRecordService.createReadingRecord(
            userBookId = userBookId,
            request = request
        )
    }

    fun getReadingRecordsByUserBookId(
        userId: UUID,
        userBookId: UUID,
        sort: String?,
        pageable: Pageable
    ): Page<ReadingRecordResponse> {
        userAuthService.validateUserExists(userId)

        userBookService.validateUserBookExists(userId, userBookId)

        return readingRecordService.getReadingRecordsByDynamicCondition(
            userBookId = userBookId,
            sort = sort,
            pageable = pageable
        )
    }
}
