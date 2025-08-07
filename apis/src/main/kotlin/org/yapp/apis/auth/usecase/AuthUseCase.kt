package org.yapp.apis.auth.usecase

import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.auth.dto.request.*
import org.yapp.apis.auth.dto.response.TokenPairResponse
import org.yapp.apis.auth.service.*
import org.yapp.apis.auth.strategy.signin.AppleAuthCredentials
import org.yapp.apis.auth.strategy.signin.SignInCredentials
import org.yapp.apis.auth.strategy.signin.SignInStrategyResolver
import org.yapp.apis.auth.strategy.withdraw.WithdrawStrategyResolver
import org.yapp.apis.user.dto.request.FindOrCreateUserRequest
import org.yapp.apis.user.dto.request.FindUserIdentityRequest
import org.yapp.apis.user.service.UserAccountService
import org.yapp.apis.user.service.UserService
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
class AuthUseCase(
    private val signInStrategyResolver: SignInStrategyResolver,
    private val withdrawStrategyResolver: WithdrawStrategyResolver,
    private val userService: UserService,
    private val userAccountService: UserAccountService,
    private val userSignInService: UserSignInService,
    private val userWithdrawalService: UserWithdrawalService,
    private val refreshTokenService: RefreshTokenService,
    private val authTokenService: AuthTokenService,
    private val appleAuthService: AppleAuthService
) {
    // 추후 Redis 저장을 비동기로 처리하고 실패 시 재시도 로직 도입
    fun signIn(socialLoginRequest: SocialLoginRequest): TokenPairResponse {
        val credentials = SocialLoginRequest.toCredentials(socialLoginRequest)
        val strategy = signInStrategyResolver.resolve(credentials)
        val userCreateInfoResponse = strategy.authenticate(credentials)

        val appleRefreshToken = fetchAppleRefreshTokenIfNeeded(credentials)

        val createUserResponse = userSignInService.processSignIn(
            FindOrCreateUserRequest.from(userCreateInfoResponse),
            appleRefreshToken
        )

        return authTokenService.generateTokenPair(GenerateTokenPairRequest.from(createUserResponse))
    }

    @Transactional
    fun reissueTokenPair(tokenRefreshRequest: TokenRefreshRequest): TokenPairResponse {
        val userIdResponse = authTokenService.validateAndGetUserIdFromRefreshToken(tokenRefreshRequest)
        authTokenService.deleteRefreshTokenForReissue(tokenRefreshRequest)

        val findUserIdentityRequest = FindUserIdentityRequest.from(userIdResponse)
        val userAuthInfoResponse = userService.findUserIdentityByUserId(findUserIdentityRequest)
        val generateTokenPairRequest = GenerateTokenPairRequest.from(userAuthInfoResponse)

        return authTokenService.generateTokenPair(generateTokenPairRequest)
    }

    @Transactional
    fun signOut(userId: UUID) {
        val refreshTokenResponse = refreshTokenService.getRefreshTokenByUserId(userId)
        authTokenService.deleteRefreshTokenForSignOutOrWithdraw(DeleteTokenRequest.from(refreshTokenResponse))
    }

    fun withdraw(userId: UUID) {
        val withdrawTargetUserResponse = userAccountService.findWithdrawUserById(userId)
        val strategy = withdrawStrategyResolver.resolve(withdrawTargetUserResponse.providerType)
        strategy.withdraw(WithdrawStrategyRequest.from(withdrawTargetUserResponse))

        userWithdrawalService.processWithdrawal(userId)
    }

    private fun fetchAppleRefreshTokenIfNeeded(credentials: SignInCredentials): String? {
        if (credentials is AppleAuthCredentials) {
            return appleAuthService.fetchAppleOauthTokens(credentials.authorizationCode).refreshToken
        }
        return null
    }
}
