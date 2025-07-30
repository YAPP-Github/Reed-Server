package org.yapp.apis.auth.exception

import org.springframework.http.HttpStatus
import org.yapp.globalutils.exception.BaseErrorCode

enum class AuthErrorCode(
    private val httpStatus: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {

    /* 400 BAD_REQUEST  */
    UNSUPPORTED_PROVIDER_TYPE(HttpStatus.BAD_REQUEST, "AUTH_400_01", "Unsupported provider type."),
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "AUTH_400_02", "Invalid credentials."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "AUTH_400_03", "Invalid request."),
    INVALID_ID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, "AUTH_400_04", "Invalid ID token format."),
    SUBJECT_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH_400_05", "Subject not found in ID token."),
    FAILED_TO_PARSE_ID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH_400_06", "Failed to parse ID token."),
    FAILED_TO_GET_USER_INFO(HttpStatus.BAD_REQUEST, "AUTH_400_07", "Failed to get user info from provider."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH_400_08", "User not found."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH_400_09", "Email not found."),

    /* 401 UNAUTHORIZED */
    INVALID_OAUTH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_401_01", "Invalid social OAuth token."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_401_02", "Invalid refresh token."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH_401_03", "Refresh token not found."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_401_04", "Invalid access token."),

    /* 409 CONFLICT */
    EMAIL_ALREADY_IN_USE(HttpStatus.CONFLICT, "AUTH_409_01", "Email already in use with a different account."),

    /* 500 INTERNAL_SERVER_ERROR */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_500_01", "Internal server error."),
    FAILED_TO_LOAD_PRIVATE_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_500_02", "Failed to load Apple private key."),
    INVALID_PRIVATE_KEY_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_500_03", "Invalid private key format."),
    FAILED_TO_COMMUNICATE_WITH_PROVIDER(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "AUTH_500_04",
        "Failed to communicate with external provider."
    ),
    OAUTH_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_500_05", "Social OAuth server error."),
    MISSING_APPLE_REFRESH_TOKEN(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "AUTH_500_06",
        "Apple did not provide a refresh token on initial login."
    );

    override fun getHttpStatus(): HttpStatus = httpStatus
    override fun getCode(): String = code
    override fun getMessage(): String = message
}
