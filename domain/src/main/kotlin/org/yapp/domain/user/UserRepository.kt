package org.yapp.domain.user

import org.yapp.domain.auth.ProviderType
import java.util.*

/**
 * Repository interface for User domain model.
 */
interface UserRepository {

    fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): User?

    fun findByEmail(email: String): User?

    fun save(user: User): User

    fun findById(id: UUID): User

    fun findByProviderTypeAndProviderIdIncludingDeleted(providerType: ProviderType, providerId: String): User?

    fun findByEmailIncludingDeleted(email: String): User?

    fun softDelete(user: User): User

    fun restore(user: User): User
}
