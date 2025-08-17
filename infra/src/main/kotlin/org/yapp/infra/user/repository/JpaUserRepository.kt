package org.yapp.infra.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.yapp.domain.user.ProviderType
import org.yapp.infra.user.entity.UserEntity
import java.util.*

/**
 * JPA repository for UserEntity.
 */
interface JpaUserRepository : JpaRepository<UserEntity, UUID> {

    fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): UserEntity?

    fun existsByEmail(email: String): Boolean

    @Query(
        value = "SELECT * FROM users u WHERE u.provider_type = :providerType AND u.provider_id = :providerId",
        nativeQuery = true
    )
    fun findByProviderTypeAndProviderIdIncludingDeleted(
        @Param("providerType") providerType: String,
        @Param("providerId") providerId: String
    ): UserEntity?
}
