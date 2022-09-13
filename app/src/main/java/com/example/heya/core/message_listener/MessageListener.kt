package com.example.heya.core.message_listener

import com.example.heya.core.model.MessageModel
import kotlinx.coroutines.flow.Flow

interface MessageListener {

    suspend fun addLastMessageListener(cb: (MessageModel) -> Unit)

    suspend fun notifyLastMessage(lastMessage: MessageModel)

    suspend fun listenToMessages(): Flow<MessageModel>

}