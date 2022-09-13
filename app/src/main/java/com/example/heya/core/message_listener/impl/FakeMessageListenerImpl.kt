package com.example.heya.core.message_listener.impl

import com.example.heya.core.data.db.MockDB
import com.example.heya.core.message_listener.MessageListener
import com.example.heya.core.model.MessageModel
import com.example.heya.core.model.MessageStatus
import com.example.heya.core.util.DateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

class FakeMessageListenerImpl : MessageListener {

    private val messageFlow by lazy { MutableSharedFlow<MessageModel>() }

    companion object {

        private data class PresetReply(val username: String, val message: String, val delay: Long)

        private val presetReplies = listOf(
            PresetReply("surya", "hello", 1000),
            PresetReply("surya", "i am great. wbu?", 5000),
            PresetReply("surya", "you'll make it this time. cheer up 😎", 10000),
        )
    }


    override suspend fun addLastMessageListener(cb: (MessageModel) -> Unit) {
        messageFlow.collect(cb)
    }

    override suspend fun notifyLastMessage(lastMessage: MessageModel) {
        messageFlow.emit(lastMessage)
    }

    override suspend fun listenToMessages() = flow<MessageModel> {

        delay(5000)
        var i = 0
        while (i < presetReplies.size) {

            delay(presetReplies[i].delay)
            val peer = presetReplies[i].username

            val receivedMessage = MessageModel(
                presetReplies[i++].message,
                DateTime.now().toISO8601Timestamp(),
                peer,
                MessageStatus.RECEIVED
            )

            MockDB.myConversations[peer].also {
                if (it == null) {
                    MockDB.myConversations[peer] = mutableListOf(receivedMessage)
                } else {
                    it.add(receivedMessage)
                }
            }

            MockDB.updateLastMessage(
                peer,
                receivedMessage.message,
                receivedMessage.iso8601Timestamp
            )
            MockDB.allUsers[peer]?.let { MockDB.previouslyContacted.add(it) }

            emit(receivedMessage)

        }
    }


}