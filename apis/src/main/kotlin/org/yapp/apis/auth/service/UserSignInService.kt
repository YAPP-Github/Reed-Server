package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.yapp.apis.user.dto.request.FindOrCreateUserRequest
import org.yapp.apis.user.dto.response.CreateUserResponse
import org.yapp.apis.user.service.UserAccountService

@Service
class UserSignInService(
    private val userAccountService: UserAccountService
) {
    fun findOrCreateUser(request: FindOrCreateUserRequest): CreateUserResponse {
        return userAccountService.findOrCreateUser(request)
    }
}
