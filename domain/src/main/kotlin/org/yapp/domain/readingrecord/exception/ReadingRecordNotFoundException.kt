package org.yapp.domain.readingrecord.exception

import org.yapp.globalutils.exception.CommonException

class ReadingRecordNotFoundException(
    errorCode: ReadingRecordErrorCode,
    message: String? = null
) : CommonException(errorCode, message)
