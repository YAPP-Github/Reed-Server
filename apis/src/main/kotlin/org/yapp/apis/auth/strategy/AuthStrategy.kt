package org.yapp.apis.auth.strategy

import org.yapp.apis.auth.dto.AuthCredentials
import org.yapp.apis.auth.dto.UserCreateInfo
import org.yapp.domain.user.ProviderType

/**
 * Strategy interface for authentication.
 * This interface defines the contract for different authentication strategies.
 */
interface AuthStrategy {

    fun getProviderType(): ProviderType

    fun authenticate(credentials: AuthCredentials): UserCreateInfo
}
