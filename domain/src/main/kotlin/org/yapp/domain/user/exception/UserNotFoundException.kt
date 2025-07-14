package org.yapp.domain.user.exception

import org.yapp.globalutils.exception.CommonException

class UserNotFoundException (
    errorCode: UserErrorCode,
    message: String? = null
) : CommonException(errorCode, message) {
}

