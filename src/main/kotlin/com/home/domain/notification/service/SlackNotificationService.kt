package com.home.domain.notification.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * 슬랙(Slack) 메신저로 알림을 발송하는 서비스 구현체
 * 설정 파일(yml)의 notification.slack.webhook-url 값을 주입받아 사용합니다.
 */
@Service
class SlackNotificationService(
    private val restTemplate: RestTemplate,
    @Value("\${notification.slack.webhook-url}") private val webhookUrl: String
) : NotificationService {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * 슬랙 웹후크(Incoming Webhook)로 메시지 발송
     * @param message 발송할 텍스트
     * @return 성공 여부 (URL 미설정 시 false 반환)
     */
    override fun send(message: String): Boolean {
        // 기본값(your-slack-webhook-url)인 경우 실제 전송을 시도하지 않음
        if (webhookUrl == "your-slack-webhook-url") {
            log.warn("Slack 웹후크 URL이 설정되지 않았습니다.")
            return false
        }
        
        return try {
            val payload = mapOf("text" to message)
            restTemplate.postForEntity(webhookUrl, payload, String::class.java)
            true
        } catch (e: Exception) {
            log.error("슬랙 메시지 발송 오류: ${e.message}")
            false
        }
    }

    override fun getMessengerName(): String = "Slack"
}
