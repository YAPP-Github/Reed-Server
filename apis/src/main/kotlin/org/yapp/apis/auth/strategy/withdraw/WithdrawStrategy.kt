package org.yapp.apis.auth.strategy.withdraw

import org.yapp.apis.auth.dto.request.WithdrawStrategyRequest
import org.yapp.domain.user.ProviderType

interface WithdrawStrategy {

    fun getProviderType(): ProviderType

    fun withdraw(request: WithdrawStrategyRequest)
}
