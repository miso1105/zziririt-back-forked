package kr.zziririt.zziririt.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: Long,
    val httpStatus: HttpStatus,
    val message: String
) {
    VALIDATION(9001, HttpStatus.BAD_REQUEST, "Validation을 통과하지 못했습니다."),
    MODEL_NOT_FOUND(9002, HttpStatus.BAD_REQUEST, "해당 Model을 찾지 못했습니다."),
    INVALID_TOKEN(9003, HttpStatus.UNAUTHORIZED, "JWT 검증에 실패하였습니다."),
}