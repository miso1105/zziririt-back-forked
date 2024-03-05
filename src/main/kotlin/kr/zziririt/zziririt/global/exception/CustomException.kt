package kr.zziririt.zziririt.global.exception

abstract class CustomException(
    open val errorCode: ErrorCode,
    override val message: String? = null,
    open val payload: Any? = null,
    override val cause: Throwable? = null
) : RuntimeException()