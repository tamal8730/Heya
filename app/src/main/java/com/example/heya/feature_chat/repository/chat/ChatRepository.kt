package com.example.heya.feature_chat.repository.chat

import com.example.heya.feature_chat.model.MessageModel
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun fetchMessages(
        peerUserName: String,
        skip: Int = 0,
        limit: Int = 20
    ): List<MessageModel>


    fun sendMessage(peerUserName: String, message: String): Flow<MessageModel>

}