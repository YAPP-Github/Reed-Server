package org.yapp.apis.home.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.yapp.apis.home.dto.response.UserHomeResponse
import org.yapp.apis.home.usecase.HomeUseCase
import java.util.*

@RestController
@RequestMapping("/api/home")
class HomeController(
    private val homeUseCase: HomeUseCase
) : HomeControllerApi {

    @GetMapping
    override fun getUserHomeData(
        @AuthenticationPrincipal userId: UUID
    ): ResponseEntity<UserHomeResponse> {
        val homeData = homeUseCase.getUserHomeData(userId)
        return ResponseEntity.ok(homeData)
    }
}
