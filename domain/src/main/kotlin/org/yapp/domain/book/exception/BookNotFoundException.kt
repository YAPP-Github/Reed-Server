package org.yapp.domain.book.exception

import org.yapp.globalutils.exception.CommonException

class BookNotFoundException(
    errorCode: BookErrorCode,
    message: String? = null
) : CommonException(errorCode, message)
