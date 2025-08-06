package org.yapp.apis.auth.strategy.withdraw

import org.springframework.stereotype.Component
import org.yapp.domain.user.ProviderType

@Component
class WithdrawStrategyResolver(
    private val strategies: List<WithdrawStrategy>
) {
    fun resolve(providerType: ProviderType): WithdrawStrategy {
        return strategies.find { it.getProviderType() == providerType }
            ?: throw IllegalArgumentException("Unsupported provider type for withdrawal: $providerType")
    }
}
