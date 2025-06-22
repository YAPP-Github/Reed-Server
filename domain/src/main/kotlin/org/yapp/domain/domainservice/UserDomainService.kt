package org.yapp.domain.user.service

import org.yapp.annotation.DomainService
import org.yapp.domain.user.User
import org.yapp.domain.user.UserRepository

@DomainService
class UserDomainService(
    private val userRepository: UserRepository
) {
    fun findOrCreate(user: User): Result<User> {
        val existingByProvider = userRepository.findByProviderTypeAndProviderId(user.providerType, user.providerId)
        if (existingByProvider != null) {
            return Result.success(existingByProvider)
        }

        val existingByEmail = userRepository.findByEmail(user.email)
        return if (existingByEmail == null) {
            val saved = userRepository.save(user)
            Result.success(saved)
        } else {
            Result.failure(IllegalStateException("Email already in use"))
        }
    }
}
