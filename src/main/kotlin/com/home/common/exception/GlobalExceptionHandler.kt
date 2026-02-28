package com.home.common.exception

import com.home.common.dto.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 애플리케이션 전역에서 발생하는 예외를 한 곳에서 관리하는 핸들러 클래스
 * 모든 예외를 ApiResponse 포맷에 맞춰 일관되게 반환합니다.
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * 비즈니스 로직(BusinessException) 예외 처리
     * 서비스 내부에서 발생하는 예상된 오류(4xx 등)를 처리합니다.
     */
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ApiResponse<Nothing>> {
        log.error("BusinessException occurred: ${e.message}")
        return ResponseEntity
            .status(e.status)
            .body(ApiResponse.error(e.status, e.message ?: "Business error"))
    }

    /**
     * @Valid 또는 @Validated 검증(MethodArgumentNotValidException) 실패 시 처리
     * 각 필드별 오류 메시지를 Map 형태의 data에 담아 반환합니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Map<String, String?>>> {
        val errors = mutableMapOf<String, String?>()
        e.bindingResult.fieldErrors.forEach { error ->
            errors[error.field] = error.defaultMessage
        }
        
        val message = "검증 실패: ${errors.keys.joinToString(", ")}"
        log.error("ValidationException occurred: $message")
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                message = message,
                data = errors
            ))
    }

    /**
     * 위에서 정의되지 않은 그 외 모든 서버 예외(Exception) 처리 (500 Error)
     * 로깅은 상세히 기록하고 사용자에게는 공통 메시지를 반환합니다.
     */
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Nothing>> {
        log.error("Unexpected error occurred: ", e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 서버 오류가 발생했습니다. 담당자에게 문의 바랍니다."))
    }
}
