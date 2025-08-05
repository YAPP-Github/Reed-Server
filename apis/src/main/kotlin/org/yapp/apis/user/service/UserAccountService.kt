package org.yapp.apis.user.service

import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.yapp.apis.user.dto.request.FindOrCreateUserRequest
import org.yapp.apis.user.dto.response.CreateUserResponse
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.domain.user.UserDomainService
import org.yapp.domain.user.vo.UserAuthVO

@Service
@Validated
class UserAccountService(
    private val userDomainService: UserDomainService
) {
    fun findOrCreateUser(@Valid findOrCreateUserRequest: FindOrCreateUserRequest): CreateUserResponse {
        userDomainService.findUserByProviderTypeAndProviderId(
            findOrCreateUserRequest.validProviderType(),
            findOrCreateUserRequest.validProviderId()
        )?.let {
            return CreateUserResponse.from(it)
        }

        userDomainService.findUserByProviderTypeAndProviderIdIncludingDeleted(
            findOrCreateUserRequest.validProviderType(),
            findOrCreateUserRequest.validProviderId()
        )?.let { deletedUserAuth ->
            val restoredUser = userDomainService.restoreDeletedUser(deletedUserAuth.id.value)
            return CreateUserResponse.from(restoredUser)
        }

        val createdUser = createNewUser(findOrCreateUserRequest)
        return CreateUserResponse.from(createdUser)
    }

    private fun createNewUser(@Valid findOrCreateUserRequest: FindOrCreateUserRequest): UserAuthVO {
        val email = findOrCreateUserRequest.getOrDefaultEmail()
        val nickname = findOrCreateUserRequest.getOrDefaultNickname()

        if (userDomainService.existsActiveUserByEmailAndDeletedAtIsNull(email)) {
            throw AuthException(AuthErrorCode.EMAIL_ALREADY_IN_USE, "Email already in use")
        }

        return userDomainService.createUser(
            email = email,
            nickname = nickname,
            profileImageUrl = findOrCreateUserRequest.profileImageUrl,
            providerType = findOrCreateUserRequest.validProviderType(),
            providerId = findOrCreateUserRequest.validProviderId()
        )
    }
}
