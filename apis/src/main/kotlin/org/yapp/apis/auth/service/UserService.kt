package org.yapp.apis.user.service

import org.springframework.stereotype.Service
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.domain.user.User
import org.yapp.domain.user.UserRepository
import org.yapp.domain.user.service.UserDomainService

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userDomainService: UserDomainService
) {

    fun findById(userId: Long): User {
        return userRepository.findById(userId)
            ?: throw AuthException(AuthErrorCode.USER_NOT_FOUND, "User not found: $userId")
    }

    fun findOrCreateUser(user: User): User {
        return userDomainService.findOrCreate(user).getOrElse {
            throw AuthException(AuthErrorCode.EMAIL_ALREADY_IN_USE, it.message)
        }
    }
}
