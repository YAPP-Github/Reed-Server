package org.yapp.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yapp.domain.auth.ProviderType
import org.yapp.user.entity.UserEntity
import java.util.*

/**
 * JPA repository for UserEntity.
 */
interface JpaUserRepository : JpaRepository<UserEntity, UUID> {

    fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): UserEntity?

    fun findByEmail(email: String): UserEntity?
}
