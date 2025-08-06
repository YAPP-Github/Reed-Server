package org.yapp.domain.user.vo

import org.yapp.domain.user.ProviderType
import org.yapp.domain.user.User

data class WithdrawTargetUserVO private constructor(
    val id: User.Id,
    val providerType: ProviderType,
    val providerId: User.ProviderId,
    val appleRefreshToken: String?
) {
    companion object {
        fun newInstance(user: User): WithdrawTargetUserVO {
            return WithdrawTargetUserVO(
                id = user.id,
                providerType = user.providerType,
                providerId = user.providerId,
                appleRefreshToken = user.appleRefreshToken
            )
        }
    }
} 
