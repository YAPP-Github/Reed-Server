package org.yapp.apis.seed.usecase

import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.auth.service.UserAuthService
import org.yapp.apis.seed.dto.response.SeedStatsResponse
import org.yapp.apis.seed.service.SeedService
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
@Transactional(readOnly = true)
class SeedUseCase(
    private val userAuthService: UserAuthService,
    private val seedService: SeedService
) {
    fun getSeedStats(userId: UUID): SeedStatsResponse {
        userAuthService.validateUserExists(userId)
        return seedService.getSeedStatsByUserId(userId)
    }
} 
