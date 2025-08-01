package org.yapp.apis.seed.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.yapp.apis.seed.dto.response.SeedStatsResponse
import org.yapp.apis.seed.usecase.SeedUseCase
import java.util.*

@RestController
@RequestMapping("/api/v1/seeds")
class SeedController(
    private val seedUseCase: SeedUseCase
) : SeedControllerApi {

    override fun getSeedStats(
        @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<SeedStatsResponse> {
        val stats = seedUseCase.getSeedStats(userId)
        return ResponseEntity.ok(stats)
    }
} 
