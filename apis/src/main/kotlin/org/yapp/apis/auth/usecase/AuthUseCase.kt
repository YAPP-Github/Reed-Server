package org.yapp.apis.auth.usecase

import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.auth.dto.request.*
import org.yapp.apis.auth.dto.response.TokenPairResponse
import org.yapp.apis.auth.dto.response.UserProfileResponse
import org.yapp.apis.auth.service.AppleAuthService
import org.yapp.apis.auth.service.AuthTokenService
import org.yapp.apis.auth.service.RefreshTokenService
import org.yapp.apis.auth.service.UserAuthService
import org.yapp.apis.auth.strategy.AppleAuthCredentials
import org.yapp.apis.auth.strategy.AuthStrategyResolver
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
@Transactional(readOnly = true)
class AuthUseCase(
    private val authStrategyResolver: AuthStrategyResolver,
    private val userAuthService: UserAuthService,
    private val refreshTokenService: RefreshTokenService,
    private val authTokenService: AuthTokenService,
    private val appleAuthService: AppleAuthService
) {
    @Transactional
    fun signIn(socialLoginRequest: SocialLoginRequest): TokenPairResponse {
        val credentials = SocialLoginRequest.toCredentials(socialLoginRequest)
        val strategy = authStrategyResolver.resolve(credentials)
        val userCreateInfoResponse = strategy.authenticate(credentials)

        val createUserResponse = userAuthService.findOrCreateUser(FindOrCreateUserRequest.from(userCreateInfoResponse))

        if (credentials is AppleAuthCredentials) {
            appleAuthService.saveAppleRefreshTokenIfMissing(
                SaveAppleRefreshTokenRequest.of(
                    createUserResponse,
                    credentials
                )
            )
        }

        return authTokenService.generateTokenPair(GenerateTokenPairRequest.from(createUserResponse))
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
        val refreshTokenResponse = refreshTokenService.getRefreshTokenByUserId(userId)
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
