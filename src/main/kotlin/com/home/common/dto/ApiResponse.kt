package com.home.common.dto

import org.springframework.http.HttpStatus

/**
 * 모든 API 응답을 위한 공통 포맷 클래스
 * @param status HTTP 상태 코드 또는 커스텀 상태 코드
 * @param message 응답 관련 안내 메시지
 * @param data 실제 반환될 데이터 객체 (성공 시에만 존재)
 */
data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null
) {
    companion object {
        /**
         * 성공 응답 생성 (기본 메시지: Success)
         */
        fun <T> success(data: T? = null, message: String = "Success"): ApiResponse<T> {
            return ApiResponse(
                status = HttpStatus.OK.value(),
                message = message,
                data = data
            )
        }

        /**
         * 에러 응답 생성
         * @param status HTTP 상태 코드 (HttpStatus Enum)
         * @param message 에러 상세 메시지
         */
        fun error(status: HttpStatus, message: String): ApiResponse<Nothing> {
            return ApiResponse(
                status = status.value(),
                message = message,
                data = null
            )
        }
    }
}
