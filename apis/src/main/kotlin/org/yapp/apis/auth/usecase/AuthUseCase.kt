package org.yapp.apis.auth.usecase

import org.yapp.apis.auth.dto.AuthCredentials
import org.yapp.apis.auth.dto.response.TokenPairResponse
import org.yapp.apis.auth.dto.response.UserProfileResponse
import org.yapp.apis.auth.helper.AuthTokenHelper
import org.yapp.apis.auth.service.SocialAuthService
import org.yapp.apis.auth.service.TokenService
import org.yapp.apis.auth.service.UserAuthService
import org.yapp.globalutils.annotation.UseCase
import java.util.*

@UseCase
class AuthUseCase(
    private val socialAuthService: SocialAuthService,
    private val userAuthService: UserAuthService,
    private val tokenService: TokenService,
    private val authTokenHelper: AuthTokenHelper
) {

    fun signIn(credentials: AuthCredentials): TokenPairResponse {
        val strategy = socialAuthService.resolve(credentials)
        val userInfo = strategy.authenticate(credentials)
        val user = userAuthService.findOrCreateUser(userInfo)
        return authTokenHelper.generateTokenPair(user.id!!)
    }

    fun refreshToken(refreshToken: String): TokenPairResponse {
        val userId = authTokenHelper.validateAndGetUserIdFromRefreshToken(refreshToken)
        authTokenHelper.deleteToken(refreshToken)
        return authTokenHelper.generateTokenPair(userId)
    }

    fun signOut(userId: UUID) {
        val refreshToken = tokenService.getRefreshTokenByUserId(userId)
        authTokenHelper.deleteToken(refreshToken.token)
    }

    fun getUserProfile(userId: UUID): UserProfileResponse {
        val user = userAuthService.findUserById(userId)
        return UserProfileResponse.of(
            id = user.id!!,
            email = user.email,
            nickname = user.nickname,
            provider = user.providerType
        )
    }

    fun getUserIdFromAccessToken(accessToken: String): UUID {
        return authTokenHelper.getUserIdFromAccessToken(accessToken)
    }
}
