package kr.zziririt.zziririt.global.exception.dto

import com.fasterxml.jackson.annotation.JsonInclude
import kr.zziririt.zziririt.global.exception.ErrorCode

data class ErrorResponse<T>(
    val code: Int,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val payload: T? = null
) {
    companion object {
        fun of(errorCode: ErrorCode): ErrorResponse<Nothing> = ErrorResponse(
            code = errorCode.code,
            message = errorCode.message
        )

        fun of(errorCode: ErrorCode, message: String): ErrorResponse<Nothing> = ErrorResponse(
            code = errorCode.code,
            message = message
        )

        fun <T> of(errorCode: ErrorCode, payload: T) = ErrorResponse(
            code = errorCode.code,
            message = errorCode.message,
            payload = payload
        )

        fun <T> of(errorCode: ErrorCode, message: String, payload: T) = ErrorResponse(
            code = errorCode.code,
            message = message,
            payload = payload
        )
    }
}