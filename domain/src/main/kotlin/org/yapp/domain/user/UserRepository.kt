package org.yapp.domain.user

import org.yapp.domain.auth.ProviderType

/**
 * Repository interface for User domain model.
 */
interface UserRepository {

    fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): User?

    fun findByEmail(email: String): User?

    fun save(user: User): User

    fun findById(id: Long): User
}
