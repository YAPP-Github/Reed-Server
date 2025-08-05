package org.yapp.apis.auth.strategy

import org.yapp.apis.auth.dto.response.UserCreateInfoResponse
import org.yapp.domain.user.ProviderType

interface AuthStrategy {

    fun getProviderType(): ProviderType

    fun authenticate(credentials: AuthCredentials): UserCreateInfoResponse
}
