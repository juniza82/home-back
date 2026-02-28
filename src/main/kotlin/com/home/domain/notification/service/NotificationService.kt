package com.home.domain.notification.service

interface NotificationService {
    fun send(message: String): Boolean
    fun getMessengerName(): String
}
