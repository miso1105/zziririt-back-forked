package kr.zziririt.zziririt.global.exception.dto

import com.fasterxml.jackson.annotation.JsonInclude
import kr.zziririt.zziririt.global.exception.ErrorCode

data class ErrorResponse(
	val code: Long,
	val message: String,
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	val payload: Any? = null
) {
	companion object {
		fun from(errorCode: ErrorCode) : ErrorResponse = ErrorResponse(
			code = errorCode.code,
			message = errorCode.message
		)
		fun from(errorCode: ErrorCode, message: String) : ErrorResponse = ErrorResponse(
			code = errorCode.code,
			message = message
		)
		fun from(errorCode: ErrorCode, payload: Any?) : ErrorResponse = ErrorResponse(
			code = errorCode.code,
			message = errorCode.message,
			payload = payload
		)
		fun from(errorCode: ErrorCode, message: String, payload: Any?) : ErrorResponse = ErrorResponse(
			code = errorCode.code,
			message = message,
			payload = payload
		)
	}
}