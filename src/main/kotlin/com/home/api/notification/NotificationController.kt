package com.home.api.notification

import com.home.common.dto.ApiResponse
import com.home.domain.notification.service.NotificationManager
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "알림 Api", description = "메신저 알림 발송 api입니다.")
class NotificationController(
    private val notificationManager: NotificationManager
) {

    @PostMapping("/send-all")
    @Operation(summary = "모든 메신저에 메시지 발송", description = "설정된 모든 메신저(Slack, Discord, Telegram)로 메시지를 전송합니다.")
    @ApiResponses(value = [
        SwaggerApiResponse(responseCode = "200", description = "성공적으로 발송됨", content = [Content(schema = Schema(implementation = ApiResponse::class))]),
        SwaggerApiResponse(responseCode = "500", description = "서버 내부 오류")
    ])
    fun sendToAll(@RequestBody request: NotificationRequest): ApiResponse<Map<String, Boolean>> {
        val result = notificationManager.sendToAll(request.message)
        return ApiResponse.success(result, "Messages processed")
    }

    @PostMapping("/{messenger}")
    @Operation(summary = "특정 메신저에 메시지 발송", description = "메신저 이름(slack, discord, telegram)을 경로에 지정하여 특정 메신저로만 메시지를 전송합니다.")
    @ApiResponses(value = [
        SwaggerApiResponse(responseCode = "200", description = "발송 성공 여부 반환", content = [Content(schema = Schema(implementation = ApiResponse::class))]),
        SwaggerApiResponse(responseCode = "400", description = "잘못된 메신저 이름")
    ])
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

    @Schema(description = "알림 발송 요청 객체")
    data class NotificationRequest(
        @Schema(description = "발송할 메시지 내용", example = "안녕하세요, 테스트 메시지입니다.")
        val message: String
    )
}
