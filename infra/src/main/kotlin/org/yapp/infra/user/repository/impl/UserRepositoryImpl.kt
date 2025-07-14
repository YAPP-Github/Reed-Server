package org.yapp.infra.user.repository.impl

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
        val userEntity = UserEntity.fromDomain(user)
        val savedEntity = jpaUserRepository.save(userEntity)
        return savedEntity.toDomain()
    }

    override fun findById(id: UUID): User? {
        return jpaUserRepository.findById(id).orElse(null)?.toDomain()
    }

    override fun existsByEmailAndDeletedAtIsNull(email: String): Boolean {
        return jpaUserRepository.existsByEmailAndDeletedAtIsNull(email)
    }

    override fun existsByIdAndDeletedAtIsNull(userId: UUID): Boolean {
        return jpaUserRepository.existsByIdAndDeletedAtIsNull(userId)
    }

    override fun findByProviderTypeAndProviderIdIncludingDeleted(
        providerType: ProviderType,
        providerId: String
    ): User? {
        return jpaUserRepository.findByProviderTypeAndProviderIdIncludingDeleted(providerType, providerId)?.toDomain()
    }
}
