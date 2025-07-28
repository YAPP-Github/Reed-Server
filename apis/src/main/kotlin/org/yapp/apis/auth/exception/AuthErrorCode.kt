package org.yapp.apis.auth.exception

import org.springframework.http.HttpStatus
import org.yapp.globalutils.exception.BaseErrorCode

/**
 * Error codes for authentication-related errors.
 */
enum class AuthErrorCode(
    private val httpStatus: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {

    /* 400 BAD_REQUEST  */
    UNSUPPORTED_PROVIDER_TYPE(HttpStatus.BAD_REQUEST, "AUTH_005", "Unsupported provider type."),
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "AUTH_006", "Invalid credentials."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "AUTH_007", "Invalid request."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH_007", "Email not found."),
    INVALID_ID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, "AUTH_008", "Invalid ID token format."),
    SUBJECT_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH_009", "Subject not found in ID token."),
    FAILED_TO_PARSE_ID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH_010", "Failed to parse ID token."),
    FAILED_TO_GET_USER_INFO(HttpStatus.BAD_REQUEST, "AUTH_011", "Failed to get user info from provider."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH_012", "User not found."),

    /* 401 UNAUTHORIZED */
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_002", "Invalid refresh token."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH_003", "Refresh token not found."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_004", "Invalid access token."),

    /* 409 CONFLICT */
    EMAIL_ALREADY_IN_USE(HttpStatus.CONFLICT, "AUTH_001", "Email already in use with a different account."),

    /* 500 INTERNAL_SERVER_ERROR */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_999", "Internal server error."),
    FAILED_TO_LOAD_PRIVATE_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_998", "Failed to load Apple private key."),
    INVALID_PRIVATE_KEY_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_997", "Invalid private key format."),
    FAILED_TO_COMMUNICATE_WITH_PROVIDER(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_996", "Failed to communicate with external provider.");


    override fun getHttpStatus(): HttpStatus = httpStatus
    override fun getCode(): String = code
    override fun getMessage(): String = message
}
