package org.yapp.apis.auth.service

import jakarta.validation.Valid
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.user.dto.request.FindOrCreateUserRequest
import org.yapp.apis.user.dto.response.CreateUserResponse
import org.yapp.apis.user.service.UserAccountService
import org.yapp.globalutils.annotation.ApplicationService

@ApplicationService
class UserSignInService(
    private val userAccountService: UserAccountService,
) {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun processSignIn(
        @Valid request: FindOrCreateUserRequest,
        appleRefreshToken: String?
    ): CreateUserResponse {
        val createUserResponse = userAccountService.findOrCreateUser(request)

        appleRefreshToken?.let {
            userAccountService.updateAppleRefreshToken(createUserResponse.id, it)
        }

        return createUserResponse
    }
}
