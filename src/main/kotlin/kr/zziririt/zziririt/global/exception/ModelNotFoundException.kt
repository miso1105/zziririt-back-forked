package kr.zziririt.zziririt.global.exception

class ModelNotFoundException : CustomException {
    constructor(errorCode: ErrorCode) : super(errorCode = errorCode)

    constructor(errorCode: ErrorCode, message: String) : super(errorCode = errorCode, message = message)

    constructor(errorCode: ErrorCode, payload: Any) : super(errorCode = errorCode, payload = payload)

    constructor(errorCode: ErrorCode, message: String, payload: Any) : super(
        message = message,
        payload = payload,
        errorCode = errorCode
    )
}