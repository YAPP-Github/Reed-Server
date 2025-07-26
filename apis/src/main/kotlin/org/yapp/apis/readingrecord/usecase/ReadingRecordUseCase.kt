package org.yapp.apis.readingrecord.usecase

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Qualifier
import org.yapp.apis.auth.service.UserAuthService
import org.yapp.apis.book.service.UserBookService
import org.yapp.apis.readingrecord.dto.request.CreateReadingRecordRequest
import org.yapp.apis.readingrecord.dto.response.ReadingRecordResponse
import org.yapp.apis.readingrecord.service.ReadingRecordService
import org.yapp.domain.readingrecord.ReadingRecordSortType
import org.yapp.globalutils.annotation.UseCase
import org.yapp.apis.book.constant.BookQueryServiceQualifier
import org.yapp.apis.book.service.BookQueryService
import org.yapp.domain.book.BookDomainService
import java.util.UUID

@UseCase
@Transactional(readOnly = true)
class ReadingRecordUseCase(
    private val readingRecordService: ReadingRecordService,
    private val userAuthService: UserAuthService,
    private val userBookService: UserBookService,
    @Qualifier(BookQueryServiceQualifier.ALADIN)
    private val bookQueryService: BookQueryService,
    private val bookDomainService: BookDomainService
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
            userId = userId,
            userBookId = userBookId,
            request = request
        )
    }

    fun getReadingRecordsByUserBookId(
        userId: UUID,
        userBookId: UUID,
        sort: ReadingRecordSortType?,
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
