package com.example.chatandroid.data.remote.dto

data class NotificationMessage(
    val `data`: Data? = null,
    val notification: Notification? = null,
    val to: String? = null
)