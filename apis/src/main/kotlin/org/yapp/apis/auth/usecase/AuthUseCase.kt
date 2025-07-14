package org.yapp.apis.auth.usecase

import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.auth.dto.request.FindOrCreateUserRequest
import org.yapp.apis.auth.dto.request.GenerateTokenPairRequest
import org.yapp.apis.auth.dto.request.SocialLoginRequest
import org.yapp.apis.auth.dto.request.TokenRefreshRequest
import org.yapp.apis.auth.dto.response.TokenPairResponse
import org.yapp.apis.auth.dto.response.UserProfileResponse
import org.yapp.apis.auth.helper.AuthTokenHelper
import org.yapp.apis.auth.service.SocialAuthService
import org.yapp.apis.auth.service.TokenService
import org.yapp.apis.auth.service.UserAuthService
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
@Transactional(readOnly = true)
class AuthUseCase(
    private val socialAuthService: SocialAuthService,
    private val userAuthService: UserAuthService,
    private val tokenService: TokenService,
    private val authTokenHelper: AuthTokenHelper
) {
    @Transactional
    fun signIn(socialLoginRequest: SocialLoginRequest): TokenPairResponse {
        val credentials = SocialLoginRequest.toCredentials(socialLoginRequest)
        val strategy = socialAuthService.resolve(credentials)

        val userCreateInfoResponse = strategy.authenticate(credentials)
        val findOrCreateUserRequest = FindOrCreateUserRequest.from(userCreateInfoResponse)
        val createUserResponse = userAuthService.findOrCreateUser(findOrCreateUserRequest)
        val generateTokenPairRequest = GenerateTokenPairRequest.from(createUserResponse)

        return authTokenHelper.generateTokenPair(
            generateTokenPairRequest.validUserId(),
            generateTokenPairRequest.validRole()
        )
    }

    @Transactional
    fun reissueTokenPair(tokenRefreshRequest: TokenRefreshRequest): TokenPairResponse {
        val refreshToken = tokenRefreshRequest.validRefreshToken()
        val userId = authTokenHelper.validateAndGetUserIdFromRefreshToken(refreshToken)
        authTokenHelper.deleteToken(refreshToken)

        val userAuthInfoResponse = userAuthService.findUserIdentityByUserId(userId)
        val generateTokenPairRequest = GenerateTokenPairRequest.from(userAuthInfoResponse)

        return authTokenHelper.generateTokenPair(
            generateTokenPairRequest.validUserId(),
            generateTokenPairRequest.validRole()
        )
    }

    @Transactional
    fun signOut(userId: UUID) {
        val refreshToken = tokenService.getRefreshTokenByUserId(userId)
        authTokenHelper.deleteToken(refreshToken.token)
    }

    fun getUserProfile(userId: UUID): UserProfileResponse {
        return userAuthService.findUserProfileByUserId(userId)
    }
}
