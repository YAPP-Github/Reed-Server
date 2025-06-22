package org.yapp.apis.util

object AuthUtils {
    fun extractUserIdFromAuthHeader(
        authorizationHeader: String,
        getUserId: (String) -> Long
    ): Long {
        val token = authorizationHeader.removePrefix("Bearer ").trim()
        return getUserId(token)
    }
}
