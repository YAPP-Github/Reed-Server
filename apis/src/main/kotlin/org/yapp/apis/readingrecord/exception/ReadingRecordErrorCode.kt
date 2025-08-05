package org.yapp.apis.readingrecord.exception

import org.springframework.http.HttpStatus
import org.yapp.globalutils.exception.BaseErrorCode

enum class ReadingRecordErrorCode(
    private val status: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {
    READING_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "READING_RECORD_404_01", "독서 기록을 찾을 수 없습니다.");

    override fun getHttpStatus(): HttpStatus = status
    override fun getCode(): String = code
    override fun getMessage(): String = message
}
