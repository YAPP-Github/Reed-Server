package org.yapp.apis.test.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.yapp.apis.test.service.TestService
import java.util.UUID

@RestController
@RequestMapping("/api/v1/test")
class TestController(
    private val testService: TestService
) {
    // Endpoints for testing with authenticated user (userId from @AuthenticationPrincipal)
    @PutMapping("/authenticated-user/last-activity/{days}-days-ago")
    fun setLastActivityForAuthenticatedUser(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable days: Int
    ) {
        testService.updateLastActivityByUserId(userId, days)
    }
}
