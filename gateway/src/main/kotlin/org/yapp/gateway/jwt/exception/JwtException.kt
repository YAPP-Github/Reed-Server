package org.yapp.gateway.jwt.exception

import org.yapp.globalutils.exception.CommonException

/**
 * Custom exception for JWT-related errors.
 */
class JwtException(
    errorCode: JwtErrorCode,
    message: String? = null
) : CommonException(errorCode, message ?: errorCode.getMessage())
