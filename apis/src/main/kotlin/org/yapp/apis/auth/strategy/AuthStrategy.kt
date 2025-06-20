package org.yapp.apis.auth.strategy

import org.yapp.apis.auth.service.AuthCredentials
import org.yapp.domain.auth.ProviderType
import org.yapp.domain.user.User

/**
 * Strategy interface for authentication.
 * This interface defines the contract for different authentication strategies.
 */
interface AuthStrategy {

    fun getProviderType(): ProviderType
    
    fun authenticate(credentials: AuthCredentials): User
}
