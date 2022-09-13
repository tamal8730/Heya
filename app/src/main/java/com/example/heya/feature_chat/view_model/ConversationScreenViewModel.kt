package com.example.heya.feature_chat.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heya.core.message_listener.MessageListener
import com.example.heya.core.model.LastMessageModel
import com.example.heya.core.model.LastMessageStatus
import com.example.heya.feature_chat.model.MessageStatus
import com.example.heya.feature_chat.repository.chat.ChatRepository
import com.example.heya.core.util.TimestampFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


sealed class Message {
    data class Sent(val message: String, val timestamp: String) : Message()
    data class Received(val message: String, val timestamp: String) : Message()
    data class Sending(val message: String, val timestamp: String) : Message()
}

sealed class ConversationScreenUIState {
    object Empty : ConversationScreenUIState()
    object Loading : ConversationScreenUIState()
    object Loaded : ConversationScreenUIState()
    object NoMessages : ConversationScreenUIState()
    data class Error(val errorMessage: String) : ConversationScreenUIState()
}


@HiltViewModel
class ConversationScreenViewModel @Inject constructor(
    @Named("chat_bubble_timestamp_formatter") private val timestampFormatter: TimestampFormatter,
    private val messageRepository: ChatRepository,
    private val messageListener: MessageListener,
) : ViewModel() {


    private val _uiState =
        MutableStateFlow<ConversationScreenUIState>(ConversationScreenUIState.Empty)
    val uiState: StateFlow<ConversationScreenUIState> = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(listOf())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    fun loadMessages(peerUserName: String) = viewModelScope.launch {

        _uiState.value = ConversationScreenUIState.Loading
        val messages = messageRepository.fetchMessages(peerUserName)

        if (messages.isEmpty()) {
            _uiState.value = ConversationScreenUIState.NoMessages
        } else {

            _messages.value = messages.map {

                when (it.status) {

                    MessageStatus.SENT -> Message.Sent(
                        message = it.message,
                        timestamp = timestampFormatter.format(it.iso8601Timestamp)
                    )

                    MessageStatus.RECEIVED -> Message.Received(
                        message = it.message,
                        timestamp = timestampFormatter.format(it.iso8601Timestamp)
                    )

                    MessageStatus.QUEUED -> Message.Sending(
                        message = it.message,
                        timestamp = timestampFormatter.format(it.iso8601Timestamp)
                    )

                }

            } as MutableList<Message>

            _uiState.value = ConversationScreenUIState.Loaded

        }
    }

    fun sendMessage(peerUserName: String, message: String) = CoroutineScope(Dispatchers.IO).launch {

        messageRepository.sendMessage(peerUserName, message).collect {

            val messageToSend =
                Message.Sending(it.message, timestampFormatter.format(it.iso8601Timestamp))

            when (it.status) {

                MessageStatus.QUEUED -> {
                    _uiState.value = ConversationScreenUIState.Loaded
                    _messages.value = _messages.value + messageToSend
                    messageListener.notifyLastMessage(
                        LastMessageModel(
                            peerUserName,
                            message,
                            it.iso8601Timestamp,
                            0,
                            LastMessageStatus.QUEUED
                        )
                    )
                }

                MessageStatus.RECEIVED -> {

                    messageListener.notifyLastMessage(
                        LastMessageModel(
                            peerUserName,
                            message,
                            it.iso8601Timestamp,
                            0,
                            LastMessageStatus.RECEIVED
                        )
                    )

                }

                MessageStatus.SENT -> {
                    _uiState.value = ConversationScreenUIState.Loaded
                    _messages.value = _messages.value.map { msg ->
                        if (msg == messageToSend)
                            Message.Sent(messageToSend.message, messageToSend.timestamp)
                        else
                            msg
                    }

                    messageListener.notifyLastMessage(
                        LastMessageModel(
                            peerUserName,
                            message,
                            it.iso8601Timestamp,
                            0,
                            LastMessageStatus.SENT
                        )
                    )


                }

            }

        }


    }

}