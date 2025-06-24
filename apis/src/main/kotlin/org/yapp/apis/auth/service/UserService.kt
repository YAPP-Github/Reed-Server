package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.domain.service.domain.UserDomainService
import org.yapp.domain.user.User
import java.util.*

@Service
class UserService(
    private val userDomainService: UserDomainService
) {

    fun findUserById(userId: UUID): User {
        return userDomainService.findById(userId)
            ?: throw AuthException(AuthErrorCode.USER_NOT_FOUND, "User not found: $userId")
    }

    fun findOrCreateUser(user: User): User {
        return userDomainService.findOrCreate(user).getOrElse {
            throw AuthException(AuthErrorCode.EMAIL_ALREADY_IN_USE, it.message)
        }
    }
}
