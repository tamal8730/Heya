package com.example.heya.feature_chat.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heya.core.message_listener.MessageListener
import com.example.heya.core.model.MessageStatus
import com.example.heya.core.util.TimestampFormatter
import com.example.heya.feature_chat.repository.chat.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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


    fun listenToMessages(peerUserName: String) = viewModelScope.launch(Dispatchers.IO) {
        messageListener.listenToMessages().collect {

            if (it.peerUsername == peerUserName) {
                _messages.value = _messages.value + Message.Received(
                    it.message, timestampFormatter.format(it.iso8601Timestamp)
                )

                messageListener.notifyLastMessage(it)
            }

        }

    }

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
                    messageListener.notifyLastMessage(it)
                }

                MessageStatus.RECEIVED -> {
                    messageListener.notifyLastMessage(it)
                }

                MessageStatus.SENT -> {
                    _uiState.value = ConversationScreenUIState.Loaded
                    _messages.value = _messages.value.map { msg ->
                        if (msg == messageToSend)
                            Message.Sent(messageToSend.message, messageToSend.timestamp)
                        else
                            msg
                    }

                    messageListener.notifyLastMessage(it)


                }

            }

        }


    }

}