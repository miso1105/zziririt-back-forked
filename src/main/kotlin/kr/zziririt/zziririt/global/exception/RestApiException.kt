package kr.zziririt.zziririt.global.exception

data class RestApiException(
    override val errorCode: ErrorCode,
    override val message: String? = null,
    override val payload: Any? = null,
    override val cause: Throwable? = null
): CustomException(errorCode, message, payload, cause)