package kr.zziririt.zziririt.global.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import kr.zziririt.zziririt.global.exception.dto.ErrorResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

private val kLogger = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(CustomException::class)
    fun handleCustomExceptionHandler(e: CustomException): ResponseEntity<Any>? {
        kLogger.warn { "handleCustomExceptionHandler: ${e.errorCode.code}-${e.errorCode.message}" }
        return if (e.message == null) {
            ResponseEntity.status(e.errorCode.httpStatus).body(ErrorResponse.from(e.errorCode))
        } else {
            ResponseEntity.status(e.errorCode.httpStatus).body(ErrorResponse.from(e.errorCode, e.message))
        }
    }

    override fun handleMethodArgumentNotValid(
        e: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorCode = ErrorCode.VALIDATION
        kLogger.warn { "handleMethodArgumentNotValid: ${errorCode.code}-${errorCode.message}" }

        return ResponseEntity.status(errorCode.httpStatus).body(ErrorResponse.from(errorCode, e.getErrorMap()))
    }

    private fun MethodArgumentNotValidException.getErrorMap(): Map<String, String?> =
        this.bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
}