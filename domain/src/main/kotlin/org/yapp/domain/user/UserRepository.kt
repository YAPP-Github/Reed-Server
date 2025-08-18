package org.yapp.domain.user

import java.util.*

interface UserRepository {

    fun findById(id: UUID): User?

    fun findByIdIncludingDeleted(id: UUID): User?

    fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): User?

    fun findByProviderTypeAndProviderIdIncludingDeleted(providerType: ProviderType, providerId: String): User?

    fun save(user: User): User

    fun existsById(id: UUID): Boolean

    fun existsByEmail(email: String): Boolean

    fun deleteById(userId: UUID): Unit
}
