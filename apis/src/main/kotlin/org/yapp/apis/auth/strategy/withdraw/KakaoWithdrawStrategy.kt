package org.yapp.apis.auth.strategy.withdraw

import org.springframework.stereotype.Component
import org.yapp.apis.auth.dto.request.WithdrawStrategyRequest
import org.yapp.apis.auth.manager.KakaoApiManager
import org.yapp.domain.user.ProviderType

@Component
class KakaoWithdrawStrategy(
    private val kakaoApiManager: KakaoApiManager
) : WithdrawStrategy {
    override fun getProviderType() = ProviderType.KAKAO

    override fun withdraw(request: WithdrawStrategyRequest) {
//        kakaoApiManager.unlink(request.providerId)
    }
}
