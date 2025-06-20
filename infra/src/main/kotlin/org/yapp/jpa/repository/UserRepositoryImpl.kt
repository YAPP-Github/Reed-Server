package org.yapp.jpa.repository

import org.springframework.stereotype.Repository
import org.yapp.domain.auth.ProviderType
import org.yapp.domain.user.User
import org.yapp.domain.user.UserEntity
import org.yapp.domain.user.UserRepository

/**
 * Implementation of UserRepository using JPA.
 */
@Repository
class UserRepositoryImpl(
    private val jpaUserRepository: JpaUserRepository
) : UserRepository {

    override fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): User? {
        return jpaUserRepository.findByProviderTypeAndProviderId(providerType, providerId)?.toDomain()
    }

    override fun findByEmail(email: String): User? {
        return jpaUserRepository.findByEmail(email)?.toDomain()
    }

    override fun save(user: User): User {
        val userEntity = UserEntity.fromDomain(user)
        val savedEntity = jpaUserRepository.save(userEntity)
        return savedEntity.toDomain()
    }

    override fun findById(id: Long): User {
        return jpaUserRepository.findById(id).get().toDomain()
    }
}
