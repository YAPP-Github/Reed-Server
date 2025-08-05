package org.yapp.apis.auth.usecase

import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.auth.dto.request.*
import org.yapp.apis.user.dto.response.CreateUserResponse
import org.yapp.apis.auth.dto.response.TokenPairResponse
import org.yapp.apis.auth.service.AppleAuthService
import org.yapp.apis.auth.service.AuthTokenService
import org.yapp.apis.auth.service.RefreshTokenService
import org.yapp.apis.auth.service.WithdrawService
import org.yapp.apis.auth.strategy.AppleAuthCredentials
import org.yapp.apis.auth.strategy.AuthCredentials
import org.yapp.apis.auth.strategy.AuthStrategyResolver
import org.yapp.apis.user.dto.request.FindOrCreateUserRequest
import org.yapp.apis.user.dto.request.FindUserIdentityRequest
import org.yapp.apis.user.service.UserAccountService
import org.yapp.apis.user.service.UserService
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
@Transactional(readOnly = true)
class AuthUseCase(
    private val authStrategyResolver: AuthStrategyResolver,
    private val userService: UserService,
    private val userAccountService: UserAccountService,
    private val refreshTokenService: RefreshTokenService,
    private val authTokenService: AuthTokenService,
    private val appleAuthService: AppleAuthService,
    private val withdrawService: WithdrawService
) {
    @Transactional
    fun signIn(socialLoginRequest: SocialLoginRequest): TokenPairResponse {
        val credentials = SocialLoginRequest.toCredentials(socialLoginRequest)
        val strategy = authStrategyResolver.resolve(credentials)
        val userCreateInfoResponse = strategy.authenticate(credentials)

        val createUserResponse =
            userAccountService.findOrCreateUser(FindOrCreateUserRequest.from(userCreateInfoResponse))

        handleAppleRefreshTokenIfNeeded(createUserResponse, credentials)

        return authTokenService.generateTokenPair(GenerateTokenPairRequest.from(createUserResponse))
    }

    @Transactional
    fun reissueTokenPair(tokenRefreshRequest: TokenRefreshRequest): TokenPairResponse {
        val userIdResponse = authTokenService.validateAndGetUserIdFromRefreshToken(tokenRefreshRequest)
        authTokenService.deleteTokenForReissue(tokenRefreshRequest)

        val findUserIdentityRequest = FindUserIdentityRequest.from(userIdResponse)
        val userAuthInfoResponse = userService.findUserIdentityByUserId(findUserIdentityRequest)
        val generateTokenPairRequest = GenerateTokenPairRequest.from(userAuthInfoResponse)

        return authTokenService.generateTokenPair(generateTokenPairRequest)
    }

    @Transactional
    fun signOut(userId: UUID) {
        val refreshTokenResponse = refreshTokenService.getRefreshTokenByUserId(userId)
        val deleteTokenRequest = DeleteTokenRequest.from(refreshTokenResponse)
        authTokenService.deleteTokenForSignOut(deleteTokenRequest)
    }

    @Transactional
    fun withdraw(userId: UUID, request: WithdrawRequest) {
        withdrawService.withdraw(userId, request)
    }

    private fun handleAppleRefreshTokenIfNeeded(
        createUserResponse: CreateUserResponse,
        credentials: AuthCredentials
    ) {
        if (credentials is AppleAuthCredentials) {
            appleAuthService.saveAppleRefreshTokenIfMissing(
                SaveAppleRefreshTokenRequest.of(createUserResponse, credentials)
            )
        }
    }
}
