package com.home.api.notification

import com.home.common.dto.ApiResponse
import com.home.domain.notification.service.NotificationManager
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "알림 Api", description = "메신저 알림 발송 api입니다.")
class NotificationController(
    private val notificationManager: NotificationManager
) {

    @PostMapping("/send-all")
    @Operation(summary = "모든 메신저에 메시지 발송")
    fun sendToAll(@RequestBody request: NotificationRequest): ApiResponse<Map<String, Boolean>> {
        val result = notificationManager.sendToAll(request.message)
        return ApiResponse.success(result, "Messages processed")
    }

    @PostMapping("/{messenger}")
    @Operation(summary = "특정 메신저에 메시지 발송")
    fun sendTo(
        @PathVariable messenger: String,
        @RequestBody request: NotificationRequest
    ): ApiResponse<Boolean> {
        val success = notificationManager.sendTo(messenger, request.message)
        return if (success) {
            ApiResponse.success(true, "Message sent to $messenger")
        } else {
            ApiResponse.success(false, "Failed to send message to $messenger. Check configuration.")
        }
    }

    data class NotificationRequest(
        val message: String
    )
}
