package com.home.common.dto

import org.springframework.http.HttpStatus

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T? = null, message: String = "Success"): ApiResponse<T> {
            return ApiResponse(
                status = HttpStatus.OK.value(),
                message = message,
                data = data
            )
        }

        fun error(status: HttpStatus, message: String): ApiResponse<Nothing> {
            return ApiResponse(
                status = status.value(),
                message = message,
                data = null
            )
        }
    }
}
