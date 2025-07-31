package org.yapp.apis.home.usecase

import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.book.service.UserBookService
import org.yapp.apis.home.dto.response.*
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
@Transactional(readOnly = true)
class HomeUseCase(
    private val userBookService: UserBookService
) {
    fun getUserHomeData(userId: UUID): UserHomeResponse {
        return userBookService.findRecentReadingBooksForHome(userId, 3)
    }
}
