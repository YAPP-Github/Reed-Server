package org.yapp.apis.auth.strategy.withdraw

import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import org.yapp.apis.auth.dto.request.WithdrawStrategyRequest
import org.yapp.domain.user.ProviderType

@Component
@Validated
class GoogleWithdrawStrategy : WithdrawStrategy {
    private val log = KotlinLogging.logger {}

    override fun getProviderType() = ProviderType.GOOGLE

    override fun withdraw(@Valid request: WithdrawStrategyRequest) {
        log.info("Starting Google withdrawal for user: ${request.userId}, providerId: ${request.providerId}")
        log.info("Successfully processed Google withdrawal for user: ${request.userId}")
    }
}
