package org.yapp.domain.token

import java.util.*


interface RefreshTokenRepository {

    fun findByUserId(userId: UUID): RefreshToken?

    fun findByToken(token: String): RefreshToken?

    fun deleteByToken(token: String)

    fun save(refreshToken: RefreshToken): RefreshToken
}
