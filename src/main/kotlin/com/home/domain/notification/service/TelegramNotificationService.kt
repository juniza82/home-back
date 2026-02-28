package com.home.domain.notification.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class TelegramNotificationService(
    private val restTemplate: RestTemplate,
    @Value("\${notification.telegram.bot-token}") private val botToken: String,
    @Value("\${notification.telegram.chat-id}") private val chatId: String
) : NotificationService {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun send(message: String): Boolean {
        if (botToken == "your-telegram-bot-token" || chatId == "your-telegram-chat-id") {
            log.warn("Telegram configuration is incomplete.")
            return false
        }
        
        val url = "https://api.telegram.org/bot$botToken/sendMessage"
        return try {
            val payload = mapOf(
                "chat_id" to chatId,
                "text" to message
            )
            restTemplate.postForEntity(url, payload, String::class.java)
            true
        } catch (e: Exception) {
            log.error("Failed to send Telegram message: ${e.message}")
            false
        }
    }

    override fun getMessengerName(): String = "Telegram"
}
