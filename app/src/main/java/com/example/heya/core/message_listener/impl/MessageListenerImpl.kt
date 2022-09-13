package com.example.heya.core.message_listener.impl

import com.example.heya.core.message_listener.MessageListener
import com.example.heya.core.model.LastMessageModel
import kotlinx.coroutines.flow.MutableSharedFlow

class MessageListenerImpl : MessageListener {

    private val messageFlow by lazy { MutableSharedFlow<LastMessageModel>() }

    override suspend fun addLastMessageListener(cb: (LastMessageModel) -> Unit) {
        messageFlow.collect(cb)
    }

    override suspend fun notifyLastMessage(lastMessage: LastMessageModel) {
        messageFlow.emit(lastMessage)
    }

}