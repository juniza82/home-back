package com.home.common.exception

import org.springframework.http.HttpStatus

open class BusinessException(
    val status: HttpStatus,
    message: String
) : RuntimeException(message)
