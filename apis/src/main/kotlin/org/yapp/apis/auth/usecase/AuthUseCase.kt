package org.yapp.apis.auth.usecase

import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.auth.dto.request.*
import org.yapp.apis.auth.dto.response.TokenPairResponse
import org.yapp.apis.auth.dto.response.UserProfileResponse
import org.yapp.apis.auth.service.AuthTokenService
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
    private val authTokenService: AuthTokenService
) {
    @Transactional
    fun signIn(socialLoginRequest: SocialLoginRequest): TokenPairResponse {
        val credentials = SocialLoginRequest.toCredentials(socialLoginRequest)
        val strategy = socialAuthService.resolve(credentials)

        val userCreateInfoResponse = strategy.authenticate(credentials)
        val findOrCreateUserRequest = FindOrCreateUserRequest.from(userCreateInfoResponse)
        val createUserResponse = userAuthService.findOrCreateUser(findOrCreateUserRequest)
        val generateTokenPairRequest = GenerateTokenPairRequest.from(createUserResponse)

        return authTokenService.generateTokenPair(generateTokenPairRequest)
    }

    @Transactional
    fun reissueTokenPair(tokenRefreshRequest: TokenRefreshRequest): TokenPairResponse {
        val userIdResponse = authTokenService.validateAndGetUserIdFromRefreshToken(tokenRefreshRequest)
        authTokenService.deleteTokenForReissue(tokenRefreshRequest)

        val findUserIdentityRequest = FindUserIdentityRequest.from(userIdResponse)
        val userAuthInfoResponse = userAuthService.findUserIdentityByUserId(findUserIdentityRequest)
        val generateTokenPairRequest = GenerateTokenPairRequest.from(userAuthInfoResponse)

        return authTokenService.generateTokenPair(generateTokenPairRequest)
    }

    @Transactional
    fun signOut(userId: UUID) {
        val refreshTokenResponse = tokenService.getRefreshTokenByUserId(userId)
        val deleteTokenRequest = DeleteTokenRequest.from(refreshTokenResponse)
        authTokenService.deleteTokenForSignOut(deleteTokenRequest)
    }

    fun getUserProfile(userId: UUID): UserProfileResponse {
        return userAuthService.findUserProfileByUserId(userId)
    }

    @Transactional
    fun updateTermsAgreement(userId: UUID, termsAgreed: Boolean): UserProfileResponse {
        return userAuthService.updateTermsAgreement(userId, termsAgreed)
    }
}
