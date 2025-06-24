package org.yapp.apis.auth.exception

import org.yapp.global.exception.CommonException

class AuthException(
    errorCode: AuthErrorCode,
    message: String? = null
) : CommonException(errorCode, message)
