package com.home.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter

class RequestFilter(
    private val objectMapper: ObjectMapper
): OncePerRequestFilter() {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private final lateinit var failureJson: String

    private val swaggerUris = arrayOf("/swagger-ui", "/v3/api-docs")
    private val lifeCycleCheckUri = "/healthcheck"
    private val cacheUri = "/cache"

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val uriStr: String = request.requestURI?.toString() ?: ""

        if (isSwaggerUri(uriStr)
            || uriStr == lifeCycleCheckUri
            || uriStr.startsWith(cacheUri)
        ) {
            log.info("$uriStr : 페이지 접근 확인")
            filterChain.doFilter(request, response)
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun setFailureResponse(response: HttpServletResponse) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.writer.write(failureJson)
        response.status = HttpStatus.FORBIDDEN.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
    }

    private fun isSwaggerUri(uriStr:String?): Boolean {
        if (uriStr == null) return false
        for (uri in swaggerUris)
            if (uriStr.startsWith(uri)) return true

        return false
    }



}