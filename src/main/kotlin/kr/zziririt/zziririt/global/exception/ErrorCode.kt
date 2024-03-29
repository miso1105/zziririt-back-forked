package kr.zziririt.zziririt.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: Int,
    val httpStatus: HttpStatus,
    val message: String
) {
    UNAUTHORIZED(1001, HttpStatus.UNAUTHORIZED, "해당 API에 대한 권한이 없습니다."),
    DUPLICATE_ROLE(1002, HttpStatus.BAD_REQUEST, "동일한 등급으로 변경할 수 없습니다."),

    POINT_POLICY_VIOLATION(4001, HttpStatus.BAD_REQUEST, "포인트는 0이상 이어야 합니다."),
    NOT_ENOUGH_POINT(4002, HttpStatus.BAD_REQUEST, "포인트 잔액이 부족합니다."),

    DUPLICATE_ICON_ID(5001, HttpStatus.BAD_REQUEST, "이미 등록된 ICON입니다."),
    NOT_ENOUGH_ICONQUANTITY(5002, HttpStatus.BAD_REQUEST, "아이콘 잔여 개수가 부족합니다."),
    NOT_FOR_SALE_STATUS(5003, HttpStatus.BAD_REQUEST, "아이콘이 판매중이 아닙니다."),
    ALREADY_HAVE_ICON(5004, HttpStatus.BAD_REQUEST, "이미 해당 아이콘을 보유하고 있습니다."),
    NOT_HAVE_ICON(5005, HttpStatus.BAD_REQUEST, "보유중인 아이콘이 아닙니다."),

    NOT_IMAGE_FILE_EXTENSION(6101, HttpStatus.BAD_REQUEST, "png, jpeg, jpg 파일만 업로드 가능합니다."),

    VALIDATION(9001, HttpStatus.BAD_REQUEST, "Validation을 통과하지 못했습니다."),
    MODEL_NOT_FOUND(9002, HttpStatus.BAD_REQUEST, "해당 Model을 찾지 못했습니다."),
    INVALID_TOKEN(9003, HttpStatus.UNAUTHORIZED, "JWT 검증에 실패하였습니다."),
    DUPLICATE_MODEL_NAME(9004, HttpStatus.BAD_REQUEST, "이미 존재하는 Model의 이름입니다."),
    ITEM_POLICY_VIOLATION(9005, HttpStatus.BAD_REQUEST, "개수는 0이상 이어야 합니다."),

}