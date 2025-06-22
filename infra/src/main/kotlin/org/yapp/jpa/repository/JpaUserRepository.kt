package org.yapp.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.yapp.domain.auth.ProviderType
import org.yapp.domain.user.UserEntity
import java.util.*

/**
 * JPA repository for UserEntity.
 */
@Repository
interface JpaUserRepository : JpaRepository<UserEntity, UUID> {

    fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): UserEntity?

    fun findByEmail(email: String): UserEntity?
}
