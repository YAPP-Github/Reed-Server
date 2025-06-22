package org.yapp.apis.auth.exception

import CommonException

class AuthException(
    errorCode: AuthErrorCode,
    message: String? = null
) : CommonException(errorCode, message)
