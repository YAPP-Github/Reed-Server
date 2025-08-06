package org.yapp.apis.auth.strategy.withdraw

import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import org.yapp.apis.auth.dto.request.WithdrawStrategyRequest
import org.yapp.apis.auth.manager.KakaoApiManager
import org.yapp.domain.user.ProviderType

@Component
@Validated
class KakaoWithdrawStrategy(
    private val kakaoApiManager: KakaoApiManager
) : WithdrawStrategy {
    private val log = KotlinLogging.logger {}

    override fun getProviderType() = ProviderType.KAKAO

    override fun withdraw(@Valid request: WithdrawStrategyRequest) {
        log.info("Starting Kakao withdrawal for user: ${request.userId}, providerId: ${request.providerId}")

        val unlinkResponse = kakaoApiManager.unlink(request.providerId)

        log.info("Successfully unlinked Kakao user. Response ID: ${unlinkResponse.id}")
    }
}
