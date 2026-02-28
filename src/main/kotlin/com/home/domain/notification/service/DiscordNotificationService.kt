package com.home.domain.notification.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class DiscordNotificationService(
    private val restTemplate: RestTemplate,
    @Value("\${notification.discord.webhook-url}") private val webhookUrl: String
) : NotificationService {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun send(message: String): Boolean {
        if (webhookUrl == "your-discord-webhook-url") {
            log.warn("Discord Webhook URL is not configured.")
            return false
        }
        
        return try {
            val payload = mapOf("content" to message)
            restTemplate.postForEntity(webhookUrl, payload, String::class.java)
            true
        } catch (e: Exception) {
            log.error("Failed to send Discord message: ${e.message}")
            false
        }
    }

    override fun getMessengerName(): String = "Discord"
}
