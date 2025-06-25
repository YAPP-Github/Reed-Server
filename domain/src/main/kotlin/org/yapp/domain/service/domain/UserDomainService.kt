package org.yapp.domain.service.domain

import org.yapp.annotation.DomainService
import org.yapp.domain.auth.ProviderType
import org.yapp.domain.user.User
import org.yapp.domain.user.UserRepository
import org.yapp.global.util.TimeProvider
import java.util.*

@DomainService
class UserDomainService(
    private val userRepository: UserRepository,
    private val timeProvider: TimeProvider
) {

    fun findById(id: UUID): User? {
        return userRepository.findById(id)
    }

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun findByProviderTypeAndProviderId(
        providerType: ProviderType,
        providerId: String
    ): User? {
        return userRepository.findByProviderTypeAndProviderId(providerType, providerId)
    }

    fun findOrCreate(user: User): Result<User> {
        val existingByProvider = findByProviderTypeAndProviderId(user.providerType, user.providerId)
        if (existingByProvider != null) {
            return Result.success(existingByProvider)
        }

        val existingByEmail = findByEmail(user.email)
        if (existingByEmail != null) {
            return Result.failure(IllegalStateException("Email already in use"))
        }

        val now = timeProvider.now()
        val newUser = User.create(
            email = user.email,
            nickname = user.nickname,
            profileImageUrl = user.profileImageUrl,
            providerType = user.providerType,
            providerId = user.providerId,
            now,
            now
        )

        val saved = save(newUser)
        return Result.success(saved)
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }
}
