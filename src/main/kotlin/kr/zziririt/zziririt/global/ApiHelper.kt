package kr.zziririt.zziririt.global

import kr.zziririt.zziririt.api.dto.CommonResponse
import kr.zziririt.zziririt.global.exception.CustomException
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun responseEntity(httpStatus: HttpStatus) =
    ResponseEntity.status(httpStatus).body(CommonResponse.of(code = httpStatus.value()))

fun <T> responseEntity(httpStatus: HttpStatus, func: () -> T) =
    func.invoke()
        .let {
            ResponseEntity
                .status(httpStatus)
                .body(
                    CommonResponse.of(code = httpStatus.value(), content = it)
                )
        }

fun <T> responseEntity(httpStatus: HttpStatus, message: String, func: () -> T) =
    func.invoke().let {
        ResponseEntity
            .status(httpStatus)
            .body(
                CommonResponse.of(code = httpStatus.value(), message = message, content = it)
            )
    }

fun responseEntity(e: CustomException): ResponseEntity<Any> = if (e.message == null) {
    ResponseEntity.status(e.errorCode.httpStatus).body(ErrorResponse.of(e.errorCode))
} else {
    ResponseEntity.status(e.errorCode.httpStatus).body(ErrorResponse.of(e.errorCode, e.message))
}

fun <T> responseEntity(errorCode: ErrorCode, payload: T): ResponseEntity<Any> =
    ResponseEntity.status(errorCode.httpStatus).body(ErrorResponse.of(errorCode, payload))