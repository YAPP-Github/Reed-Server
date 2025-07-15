package org.yapp.domain.user

import java.util.*

/**
 * Repository interface for User domain model.
 */
interface UserRepository {

    fun findById(id: UUID): User?

    fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): User?

    fun findByProviderTypeAndProviderIdIncludingDeleted(providerType: ProviderType, providerId: String): User?

    fun save(user: User): User

    fun existsById(userId: UUID): Boolean

    fun existsByEmail(email: String): Boolean
}
