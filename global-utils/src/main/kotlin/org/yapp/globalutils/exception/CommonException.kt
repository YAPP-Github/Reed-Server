package org.yapp.globalutils.exception

import org.springframework.web.server.ResponseStatusException

open class CommonException(
    val errorCode: BaseErrorCode,
    message: String? = null
) : ResponseStatusException(
    errorCode.getHttpStatus(),
    message ?: errorCode.getMessage()
)
