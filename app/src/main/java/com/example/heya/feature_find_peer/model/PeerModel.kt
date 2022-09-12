package com.example.heya.feature_find_peer.model

data class PeerModel(
    val userName: String,
    val imageURL: String,
    val lastMessage: String,
    val lastMessageTimestamp: String,
    val unreadMessageCount: Int = 0
)