package org.yapp.domain.service.domain

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

    fun findByProvider(profile: SocialUserProfile): User? =
        userRepository.findByProviderTypeAndProviderId(profile.providerType, profile.providerId)

    fun findDeletedByProvider(profile: SocialUserProfile): User? =
        userRepository.findByProviderTypeAndProviderIdIncludingDeleted(profile.providerType, profile.providerId)

    fun findDeletedByEmail(email: String): User? =
        userRepository.findByEmailIncludingDeleted(email)

    fun existsActiveByEmail(email: String): Boolean =
        findByEmail(email) != null

    fun existsDeletedByEmailWithDifferentProvider(profile: SocialUserProfile): Boolean {
        val deletedUser = findDeletedByEmail(profile.email)
        return deletedUser != null && deletedUser.providerType != profile.providerType
    }

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

    fun restore(user: User): User =
        userRepository.restore(user)

    fun save(user: User): User =
        userRepository.save(user)
}
