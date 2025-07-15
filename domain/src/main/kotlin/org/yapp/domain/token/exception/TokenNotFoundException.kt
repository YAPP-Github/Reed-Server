package org.yapp.domain.token.exception

import org.yapp.globalutils.exception.CommonException

class TokenNotFoundException(
    errorCode: TokenErrorCode,
    message: String? = null
) : CommonException(errorCode, message)

