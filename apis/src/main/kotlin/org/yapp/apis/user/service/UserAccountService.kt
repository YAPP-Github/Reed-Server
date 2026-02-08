package org.yapp.apis.user.service

import jakarta.validation.Valid
import org.yapp.apis.user.dto.request.SaveAppleRefreshTokenRequest
import org.yapp.apis.user.dto.request.SaveGoogleRefreshTokenRequest
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.user.dto.request.FindOrCreateUserRequest
import org.yapp.apis.user.dto.response.CreateUserResponse
import org.yapp.apis.user.dto.response.WithdrawTargetUserResponse
import org.yapp.domain.user.UserDomainService
import org.yapp.domain.user.vo.UserAuthVO
import org.yapp.globalutils.annotation.ApplicationService
import java.util.*

@ApplicationService
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

    fun findWithdrawUserById(userId: UUID): WithdrawTargetUserResponse {
        val withdrawTargetUserVO = userDomainService.findWithdrawUserById(userId)
        return WithdrawTargetUserResponse.from(withdrawTargetUserVO)
    }

    fun withdrawUser(userId: UUID) {
        userDomainService.deleteUser(userId)
    }

    fun updateAppleRefreshToken(@Valid saveAppleRefreshTokenRequest: SaveAppleRefreshTokenRequest): CreateUserResponse {
        val userAuthVO = userDomainService.updateAppleRefreshToken(
            saveAppleRefreshTokenRequest.validUserId(),
            saveAppleRefreshTokenRequest.validAppleRefreshToken()
        )

        return CreateUserResponse.from(userAuthVO)
    }

    fun updateGoogleRefreshToken(@Valid saveGoogleRefreshTokenRequest: SaveGoogleRefreshTokenRequest): CreateUserResponse {
        val userAuthVO = userDomainService.updateGoogleRefreshToken(
            saveGoogleRefreshTokenRequest.validUserId(),
            saveGoogleRefreshTokenRequest.validGoogleRefreshToken()
        )

        return CreateUserResponse.from(userAuthVO)
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
