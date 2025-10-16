package org.yapp.infra.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.yapp.domain.user.ProviderType
import org.yapp.infra.user.entity.UserEntity
import java.time.LocalDateTime
import java.util.*

/**
 * JPA repository for UserEntity.
 */
interface JpaUserRepository : JpaRepository<UserEntity, UUID> {

    fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): UserEntity?

    fun existsByEmail(email: String): Boolean

    @Query(
        value = "SELECT u.* FROM users u WHERE u.provider_type = :#{#providerType.name()} AND u.provider_id = :providerId",
        nativeQuery = true
    )
    fun findByProviderTypeAndProviderIdIncludingDeleted(
        providerType: ProviderType,
        providerId: String
    ): UserEntity?

    @Query(
        value = "SELECT u.* FROM users u WHERE u.id = :#{#id.toString()}",
        nativeQuery = true
    )
    fun findByIdIncludingDeleted(id: UUID): UserEntity?

    /**
     * Find users who haven't been active since the specified time and have notifications enabled
     */
    fun findByLastActivityBeforeAndNotificationEnabledAndDeletedAtIsNull(
        lastActivityBefore: LocalDateTime, 
        notificationEnabled: Boolean
    ): List<UserEntity>
}
