package org.yapp.domain.service.domain

import org.yapp.domain.auth.ProviderType
import org.yapp.domain.user.User
import org.yapp.domain.user.UserRepository
import org.yapp.domain.user.vo.SocialUserProfile
import org.yapp.globalutils.annotation.DomainService
import org.yapp.globalutils.util.TimeProvider
import java.util.*

@DomainService
class UserDomainService(
    private val userRepository: UserRepository,
    private val timeProvider: TimeProvider
) {

    fun findById(id: UUID): User? =
        userRepository.findById(id)

    fun findByEmail(email: String): User? =
        userRepository.findByEmail(email)

    fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): User? =
        userRepository.findByProviderTypeAndProviderId(providerType, providerId)

    fun findByProviderTypeAndProviderIdIncludingDeleted(providerType: ProviderType, providerId: String): User? =
        userRepository.findByProviderTypeAndProviderIdIncludingDeleted(providerType, providerId)

    fun existsActiveByEmail(email: String): Boolean =
        findByEmail(email) != null

    fun create(profile: SocialUserProfile): User {
        val now = timeProvider.now()
        val user = User.create(
            email = profile.email,
            nickname = profile.nickname,
            profileImageUrl = profile.profileImageUrl,
            providerType = profile.providerType,
            providerId = profile.providerId,
            createdAt = now,
            updatedAt = now
        )
        return save(user)
    }

    fun restoreDeletedUser(deletedUser: User): User =
        save(deletedUser.restore())

    fun save(user: User): User =
        userRepository.save(user)

    fun existsById(userId: UUID): Boolean {
        return userRepository.existsById(userId)
    }
}
