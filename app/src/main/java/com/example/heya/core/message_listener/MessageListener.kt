package com.example.heya.core.message_listener

import com.example.heya.core.model.LastMessageModel

interface MessageListener {

    suspend fun addLastMessageListener(cb: (LastMessageModel) -> Unit)

    suspend fun notifyLastMessage(lastMessage: LastMessageModel)

}