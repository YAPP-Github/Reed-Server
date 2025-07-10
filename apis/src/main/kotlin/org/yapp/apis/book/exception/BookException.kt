package org.yapp.apis.book.exception

import org.yapp.globalutils.exception.CommonException

class BookException(
    errorCode: BookErrorCode,
    message: String? = null
) : CommonException(errorCode, message)
