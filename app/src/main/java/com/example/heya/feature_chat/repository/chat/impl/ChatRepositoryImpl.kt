package com.example.heya.feature_chat.repository.chat.impl

import com.example.heya.core.data.db.MockDB
import com.example.heya.core.util.DateTime
import com.example.heya.feature_chat.model.MessageModel
import com.example.heya.feature_chat.model.MessageStatus
import com.example.heya.feature_chat.repository.chat.ChatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.util.*

class ChatRepositoryImpl : ChatRepository {

    override suspend fun fetchMessages(
        peerUserName: String,
        skip: Int,
        limit: Int
    ): List<MessageModel> {
        delay(1000)
        return MockDB.myConversations[peerUserName]?.sortedBy { it.iso8601Timestamp } ?: listOf()
    }

    private suspend fun enqueueMessage(
        peerUserName: String,
        message: String,
        timestamp: String,
    ): MessageModel {

        val messageModel = MessageModel(
            message,
            timestamp,
            peerUserName,
            MessageStatus.QUEUED
        )

        delay(1000)
        if (MockDB.messageQueue[peerUserName] == null) {
            MockDB.messageQueue[peerUserName] = LinkedList<MessageModel>()
        }

        MockDB.messageQueue[peerUserName]?.add(messageModel)
        return messageModel
    }

    private suspend fun dequeueMessage(peerUserName: String): MessageModel? {
        delay(1000)
        return MockDB.messageQueue[peerUserName]?.poll()
    }

    override fun sendMessage(peerUserName: String, message: String) = flow {

        // enqueue
        val now = DateTime.now().toISO8601Timestamp()
        emit(MessageModel(message, now, peerUserName, MessageStatus.QUEUED))
        enqueueMessage(peerUserName, message, now)

        // send
        delay(5000)

        //dequeue
        val sentMessage = dequeueMessage(peerUserName)?.copy(status = MessageStatus.SENT)
            ?: throw Exception("message was not sent")
        MockDB.myConversations[peerUserName].also {
            if (it == null) {
                MockDB.myConversations[peerUserName] = mutableListOf(sentMessage)
            } else {
                it.add(sentMessage)
            }
        }
        MockDB.updateLastMessage(peerUserName, sentMessage.message, sentMessage.iso8601Timestamp)
        MockDB.allUsers[peerUserName]?.let { MockDB.previouslyContacted.add(it) }
        emit(sentMessage)

    }

}