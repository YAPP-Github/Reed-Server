package org.yapp.domain.service.domain

import org.yapp.annotation.DomainService
import org.yapp.domain.auth.ProviderType
import org.yapp.domain.user.User
import org.yapp.domain.user.UserRepository
import java.util.*

@DomainService
class UserDomainService(
    private val userRepository: UserRepository
) {

    fun findById(id: UUID): User? {
        return userRepository.findById(id)
    }

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun findByProviderTypeAndProviderId(
        providerType: ProviderType, providerId: String
    ): User? {
        return userRepository.findByProviderTypeAndProviderId(providerType, providerId)
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }

    fun findOrCreate(user: User): Result<User> {
        val existingByProvider = findByProviderTypeAndProviderId(user.providerType, user.providerId)
        if (existingByProvider != null) {
            return Result.success(existingByProvider)
        }

        val existingByEmail = findByEmail(user.email)
        return if (existingByEmail == null) {
            val saved = save(user)
            Result.success(saved)
        } else {
            Result.failure(IllegalStateException("Email already in use"))
        }
    }
}
