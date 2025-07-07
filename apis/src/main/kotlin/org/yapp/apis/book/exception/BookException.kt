package org.yapp.apis.auth.exception

import org.yapp.domain.book.exception.BookErrorCode
import org.yapp.globalutils.exception.CommonException

class BookException(
    errorCode: BookErrorCode,
    message: String? = null
) : CommonException(errorCode, message)
