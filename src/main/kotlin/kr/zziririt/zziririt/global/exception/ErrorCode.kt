package kr.zziririt.zziririt.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: Int,
    val httpStatus: HttpStatus,
    val message: String
) {
    UNAUTHORIZED(1001, HttpStatus.UNAUTHORIZED, "해당 API에 대한 권한이 없습니다."),

    NOT_IMAGE_FILE_EXTENSION(6101, HttpStatus.BAD_REQUEST, "png, jpeg, jpg 파일만 업로드 가능합니다."),

    VALIDATION(9001, HttpStatus.BAD_REQUEST, "Validation을 통과하지 못했습니다."),
    MODEL_NOT_FOUND(9002, HttpStatus.BAD_REQUEST, "해당 Model을 찾지 못했습니다."),
    INVALID_TOKEN(9003, HttpStatus.UNAUTHORIZED, "JWT 검증에 실패하였습니다."),
    DUPLICATE_MODEL_NAME(9004, HttpStatus.BAD_REQUEST, "이미 존재하는 Model의 이름입니다."),
}