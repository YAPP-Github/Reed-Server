package org.yapp.apis.book.exception

import org.springframework.http.HttpStatus
import org.yapp.globalutils.exception.BaseErrorCode


enum class BookErrorCode(
    private val httpStatus: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {
    /* 500 INTERNAL_SERVER_ERROR */
    ALADIN_API_SEARCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "BOOK_001", "알라딘 도서 검색 API 호출에 실패했습니다."),
    ALADIN_API_LOOKUP_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "BOOK_002", "알라딘 도서 상세 조회 API 호출에 실패했습니다.");

    override fun getHttpStatus(): HttpStatus = httpStatus
    override fun getCode(): String = code
    override fun getMessage(): String = message
}
