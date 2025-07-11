package org.yapp.domain.user

import org.yapp.domain.auth.ProviderType
import java.util.*

/**
 * Repository interface for User domain model.
 */
interface UserRepository {

    fun findById(id: UUID): User

    fun findByEmail(email: String): User?

    fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): User?

    fun findByProviderTypeAndProviderIdIncludingDeleted(providerType: ProviderType, providerId: String): User?

    fun save(user: User): User
    
    fun existsById(id: UUID): Boolean

}
