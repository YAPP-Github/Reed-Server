package org.yapp.apis.seed.usecase

import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.seed.dto.response.SeedStatsResponse
import org.yapp.apis.seed.service.SeedService
import org.yapp.apis.user.service.UserService
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
@Transactional(readOnly = true)
class SeedUseCase(
    private val userService: UserService,
    private val seedService: SeedService
) {
    fun getSeedStats(userId: UUID): SeedStatsResponse {
        userService.validateUserExists(userId)
        return seedService.getSeedStatsByUserId(userId)
    }
} 
