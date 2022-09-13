package com.example.heya.core.model

enum class LastMessageStatus {
    QUEUED, SENT, RECEIVED
}

data class LastMessageModel(
    val peerUsername: String,
    val message: String,
    val timestamp: String,
    val unreadCount: Int,
    val status: LastMessageStatus
)