package org.yapp.domain.domainservice

import org.yapp.annotation.DomainService
import org.yapp.domain.auth.ProviderType
import org.yapp.domain.user.User
import org.yapp.domain.user.UserRepository
import java.util.*

@DomainService
class UserDomainServiceImpl(
    private val userRepository: UserRepository
) : UserDomainService {

    override fun findById(id: UUID): User? {
        return userRepository.findById(id)
    }

    override fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    override fun findByProviderTypeAndProviderId(
        providerType: ProviderType,
        providerId: String
    ): User? {
        return userRepository.findByProviderTypeAndProviderId(providerType, providerId)
    }

    override fun save(user: User): User {
        return userRepository.save(user)
    }

    override fun findOrCreate(user: User): Result<User> {
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
