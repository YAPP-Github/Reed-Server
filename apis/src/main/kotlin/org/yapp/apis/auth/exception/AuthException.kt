package org.yapp.apis.auth.exception

import org.yapp.globalutils.exception.CommonException

class AuthException(
    errorCode: AuthErrorCode,
    message: String? = null
) : CommonException(errorCode, message)
