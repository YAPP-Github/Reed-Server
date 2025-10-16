package org.yapp.domain.user

import java.time.LocalDateTime
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

    /**
     * Find users who haven't been active since the specified time and have notifications enabled
     * 
     * @param lastActivityBefore Find users whose last activity is before this time
     * @param notificationEnabled Find users with notifications enabled if true
     * @return List of users matching the criteria
     */
    fun findByLastActivityBeforeAndNotificationEnabled(lastActivityBefore: LocalDateTime, notificationEnabled: Boolean): List<User>
}
