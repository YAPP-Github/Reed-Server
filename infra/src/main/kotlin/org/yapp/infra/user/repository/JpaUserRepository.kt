package org.yapp.infra.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.yapp.domain.auth.ProviderType
import org.yapp.infra.user.entity.UserEntity
import java.util.*

/**
 * JPA repository for UserEntity.
 */
interface JpaUserRepository : JpaRepository<UserEntity, UUID> {

    fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): UserEntity?

    fun findByEmail(email: String): UserEntity?

    @Query("SELECT u FROM UserEntity u WHERE u.providerType = :providerType AND u.providerId = :providerId")
    fun findByProviderTypeAndProviderIdIncludingDeleted(providerType: ProviderType, providerId: String): UserEntity?
}
