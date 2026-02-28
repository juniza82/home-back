package com.home.common.exception

import com.home.common.dto.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * 비즈니스 로직 예외 처리
     */
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ApiResponse<Nothing>> {
        log.error("BusinessException: ${e.message}")
        return ResponseEntity
            .status(e.status)
            .body(ApiResponse.error(e.status, e.message ?: "Business error"))
    }

    /**
     * @Valid 검증 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Map<String, String?>>> {
        val errors = mutableMapOf<String, String?>()
        e.bindingResult.fieldErrors.forEach { error ->
            errors[error.field] = error.defaultMessage
        }
        
        val message = "Validation failed for fields: ${errors.keys.joinToString(", ")}"
        log.error("ValidationException: $message")
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                message = message,
                data = errors
            ))
    }

    /**
     * 그 외 모든 예외 처리
     */
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Nothing>> {
        log.error("Unexpected error: ", e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: ${e.message}"))
    }
}
