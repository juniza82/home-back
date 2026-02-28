package com.home.domain.notification.service

import org.springframework.stereotype.Service

/**
 * 여러 메신저 알림 서비스(Slack, Discord, Telegram 등)를 총괄하여 관리하는 클래스
 * 모든 구현된 NotificationService 빈(Bean) 리스트를 주입받아 사용합니다.
 */
@Service
class NotificationManager(
    private val notificationServices: List<NotificationService>
) {
    /**
     * 주입된 모든 메신저에 동일한 메시지를 발송
     * @param message 발송할 메시지 본문
     * @return 각 메신저 이름별 발송 성공 여부 (Map<String, Boolean>)
     */
    fun sendToAll(message: String): Map<String, Boolean> {
        return notificationServices.associate { 
            it.getMessengerName() to it.send(message)
        }
    }

    /**
     * 특정 메신저 이름을 지정하여 메시지 발송
     * @param messengerName 발송할 메신저 이름 (대소문자 무시: slack, discord, telegram)
     * @param message 발송할 메시지 본문
     * @return 발송 성공 여부
     */
    fun sendTo(messengerName: String, message: String): Boolean {
        return notificationServices.find { 
            it.getMessengerName().equals(messengerName, ignoreCase = true) 
        }?.send(message) ?: false
    }
}
