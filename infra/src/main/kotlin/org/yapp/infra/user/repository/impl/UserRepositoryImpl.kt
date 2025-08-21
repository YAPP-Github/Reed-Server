package org.yapp.infra.user.repository.impl

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.yapp.domain.user.ProviderType
import org.yapp.domain.user.User
import org.yapp.domain.user.UserRepository
import org.yapp.infra.user.entity.UserEntity
import org.yapp.infra.user.repository.JpaUserRepository
import java.util.*

@Repository
class UserRepositoryImpl(
    private val jpaUserRepository: JpaUserRepository
) : UserRepository {

    override fun findByProviderTypeAndProviderId(providerType: ProviderType, providerId: String): User? {
        return jpaUserRepository.findByProviderTypeAndProviderId(providerType, providerId)?.toDomain()
    }

    override fun save(user: User): User {
        val savedEntity = jpaUserRepository.saveAndFlush(UserEntity.fromDomain(user))
        return savedEntity.toDomain()
    }

    override fun findById(id: UUID): User? {
        return jpaUserRepository.findByIdOrNull(id)?.toDomain()
    }

    override fun findByIdIncludingDeleted(id: UUID): User? {
        return jpaUserRepository.findByIdIncludingDeleted(id)?.toDomain()
    }

    override fun existsById(id: UUID): Boolean {
        return jpaUserRepository.existsById(id)
    }

    override fun existsByEmail(email: String): Boolean {
        return jpaUserRepository.existsByEmail(email)
    }

    override fun findByProviderTypeAndProviderIdIncludingDeleted(
        providerType: ProviderType,
        providerId: String
    ): User? {
        return jpaUserRepository.findByProviderTypeAndProviderIdIncludingDeleted(providerType, providerId)?.toDomain()
    }

    override fun deleteById(userId: UUID) {
        return jpaUserRepository.deleteById(userId)
    }
}
