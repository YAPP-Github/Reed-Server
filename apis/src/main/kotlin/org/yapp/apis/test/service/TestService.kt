package org.yapp.apis.test.service

import org.springframework.stereotype.Service
import org.yapp.domain.user.UserDomainService
import java.time.LocalDateTime
import java.util.UUID

@Service
class TestService(
    private val userDomainService: UserDomainService,
) {

    fun updateLastActivityByUserId(userId: UUID, days: Int) {
        val newLastActivity = LocalDateTime.now().minusDays(days.toLong())
        userDomainService.forceUpdateLastActivity(userId, newLastActivity)
    }
}
