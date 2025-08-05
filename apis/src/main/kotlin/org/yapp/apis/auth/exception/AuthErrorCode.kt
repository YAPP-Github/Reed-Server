package org.yapp.apis.auth.exception

import org.springframework.http.HttpStatus
import org.yapp.globalutils.exception.BaseErrorCode

enum class AuthErrorCode(
    private val httpStatus: HttpStatus,
    private val code: String,
    private val message: String
) : BaseErrorCode {

    /* 400 BAD_REQUEST  */
    UNSUPPORTED_PROVIDER_TYPE(HttpStatus.BAD_REQUEST, "AUTH_400_01", "지원되지 않는 공급자 타입입니다."),
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "AUTH_400_02", "잘못된 인증 정보입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "AUTH_400_03", "잘못된 요청입니다."),
    INVALID_ID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, "AUTH_400_04", "잘못된 ID 토큰 형식입니다."),
    SUBJECT_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH_400_05", "ID 토큰에서 주체를 찾을 수 없습니다."),
    FAILED_TO_PARSE_ID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH_400_06", "ID 토큰 파싱에 실패했습니다."),
    FAILED_TO_GET_USER_INFO(HttpStatus.BAD_REQUEST, "AUTH_400_07", "공급자로부터 사용자 정보를 가져오는데 실패했습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH_400_08", "사용자를 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH_400_09", "이메일을 찾을 수 없습니다."),
    INVALID_APPLE_ID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH_400_10", "유효하지 않은 Apple ID 토큰입니다."),
    PROVIDER_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "AUTH_400_11", "요청된 공급자 타입이 실제 사용자의 공급자 타입과 일치하지 않습니다."),
    APPLE_REFRESH_TOKEN_MISSING(HttpStatus.BAD_REQUEST, "AUTH_400_12", "Apple 사용자 탈퇴 시 리프레시 토큰이 누락되었습니다."),

    /* 401 UNAUTHORIZED */
    INVALID_OAUTH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_401_01", "잘못된 소셜 OAuth 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_401_02", "잘못된 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH_401_03", "리프레시 토큰을 찾을 수 없습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_401_04", "잘못된 액세스 토큰입니다."),

    /* 403 FORBIDDEN */
    INSUFFICIENT_PERMISSIONS(HttpStatus.FORBIDDEN, "AUTH_403_01", "요청된 리소스에 대한 권한이 부족합니다."),

    /* 409 CONFLICT */
    EMAIL_ALREADY_IN_USE(HttpStatus.CONFLICT, "AUTH_409_01", "이미 다른 계정에서 사용 중인 이메일입니다."),

    /* 500 INTERNAL_SERVER_ERROR */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_500_01", "내부 서버 오류입니다."),
    FAILED_TO_LOAD_PRIVATE_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_500_02", "Apple 개인키 로드에 실패했습니다."),
    INVALID_PRIVATE_KEY_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_500_03", "잘못된 개인키 형식입니다."),
    FAILED_TO_COMMUNICATE_WITH_PROVIDER(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "AUTH_500_04",
        "외부 공급자와의 통신에 실패했습니다."
    ),
    OAUTH_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_500_05", "소셜 OAuth 서버 오류입니다."),
    MISSING_APPLE_REFRESH_TOKEN(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "AUTH_500_06",
        "Apple에서 초기 로그인 시 리프레시 토큰을 제공하지 않았습니다."
    );

    override fun getHttpStatus(): HttpStatus = httpStatus
    override fun getCode(): String = code
    override fun getMessage(): String = message
}
