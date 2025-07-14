package org.yapp.domain.user

import org.yapp.domain.user.ProviderType
import java.util.*

/**
 * Repository interface for User domain model.
 */
interface UserRepository {

    fun findById(id: UUID): User?

    fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): User?

    fun findByProviderTypeAndProviderIdIncludingDeleted(providerType: ProviderType, providerId: String): User?

    fun save(user: User): User

    fun existsByIdAndDeletedAtIsNull(userId: UUID): Boolean

    fun existsByEmailAndDeletedAtIsNull(email: String): Boolean
}
