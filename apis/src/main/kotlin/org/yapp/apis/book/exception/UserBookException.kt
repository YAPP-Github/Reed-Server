package org.yapp.apis.book.exception

import org.yapp.globalutils.exception.CommonException

class UserBookException (
    errorCode: UserBookErrorCode,
    message: String? = null
) : CommonException(errorCode, message)
