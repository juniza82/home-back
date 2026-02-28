package com.home.domain.notification.service

import org.springframework.stereotype.Service

@Service
class NotificationManager(
    private val notificationServices: List<NotificationService>
) {
    fun sendToAll(message: String): Map<String, Boolean> {
        return notificationServices.associate { 
            it.getMessengerName() to it.send(message)
        }
    }

    fun sendTo(messengerName: String, message: String): Boolean {
        return notificationServices.find { 
            it.getMessengerName().equals(messengerName, ignoreCase = true) 
        }?.send(message) ?: false
    }
}
