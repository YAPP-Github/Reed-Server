package org.yapp.apis.home.usecase

import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.home.service.HomeService
import org.yapp.apis.home.dto.response.*
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
@Transactional(readOnly = true)
class HomeUseCase(
    private val homeService: HomeService
) {
    fun getUserHomeData(userId: UUID, limit: Int = 3): UserHomeResponse {
        val validatedLimit = limit.coerceIn(1, 10)
        return homeService.getUserHomeData(userId, validatedLimit)
    }
}
