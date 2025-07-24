package org.yapp.apis.book.exception

import org.yapp.globalutils.exception.CommonException

class UserBookNotFoundException(
    errorCode: UserBookErrorCode,
    message: String? = null
) : CommonException(errorCode, message)
