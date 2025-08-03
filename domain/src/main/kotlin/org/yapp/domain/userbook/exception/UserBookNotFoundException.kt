package org.yapp.domain.userbook.exception

import org.yapp.domain.user.exception.UserErrorCode
import org.yapp.globalutils.exception.CommonException

class UserBookNotFoundException (
    errorCode: UserBookErrorCode,
    message: String? = null
) : CommonException(errorCode, message)
