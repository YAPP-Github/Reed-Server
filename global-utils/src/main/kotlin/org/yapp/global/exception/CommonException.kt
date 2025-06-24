import org.springframework.web.server.ResponseStatusException
import org.yapp.global.exception.BaseErrorCode

open class CommonException(
    val errorCode: BaseErrorCode,
    message: String? = null
) : ResponseStatusException(
    errorCode.getHttpStatus(),
    message ?: errorCode.getMessage()
)