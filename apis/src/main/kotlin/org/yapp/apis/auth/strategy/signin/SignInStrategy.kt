package org.yapp.apis.auth.strategy.signin

import org.yapp.apis.auth.dto.response.UserCreateInfoResponse
import org.yapp.domain.user.ProviderType

interface SignInStrategy {

    fun getProviderType(): ProviderType

    fun authenticate(credentials: SignInCredentials): UserCreateInfoResponse
}
