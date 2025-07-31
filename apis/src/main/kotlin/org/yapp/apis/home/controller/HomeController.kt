package org.yapp.apis.home.controller

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.yapp.apis.home.dto.response.UserHomeResponse
import org.yapp.apis.home.usecase.HomeUseCase
import java.util.*

@Validated
@RestController
@RequestMapping("/api/v1/home")
class HomeController(
    private val homeUseCase: HomeUseCase
) : HomeControllerApi {

    @GetMapping
    override fun getUserHomeData(
        @AuthenticationPrincipal userId: UUID,
        @RequestParam(defaultValue = "3") @Min(1) @Max(10) limit: Int
    ): ResponseEntity<UserHomeResponse> {
        val homeData = homeUseCase.getUserHomeData(userId, limit)
        return ResponseEntity.ok(homeData)
    }
}
