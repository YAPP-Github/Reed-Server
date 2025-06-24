package org.yapp.domain.domainservice

import org.yapp.domain.auth.ProviderType
import org.yapp.domain.user.User
import java.util.*

interface UserDomainService {
    fun findById(id: UUID): User?
    fun findByEmail(email: String): User?
    fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): User?
    fun save(user: User): User
    fun findOrCreate(user: User): Result<User>
}
