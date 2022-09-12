package com.example.heya.feature_chat.model

enum class MessageStatus {
    SENT, RECEIVED, QUEUED
}

data class MessageModel(
    val message: String,
    val iso8601Timestamp: String,
    val peerUsername: String,
    val status: MessageStatus
)