package org.yapp.apis.util

import java.util.*

object AuthUtils {
    fun extractUserIdFromAuthHeader(
        authorizationHeader: String,
        getUserId: (String) -> UUID
    ): UUID {
        val token = authorizationHeader.removePrefix("Bearer ").trim()
        return getUserId(token)
    }
}
