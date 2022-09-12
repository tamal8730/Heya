package com.example.heya.feature_chat.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.heya.feature_chat.model.MessageStatus
import com.example.heya.feature_chat.repository.chat.ChatRepository
import com.example.heya.core.util.TimestampFormatter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


sealed class Message {
    data class Sent(val message: String, val timestamp: String) : Message()
    data class Received(val message: String, val timestamp: String) : Message()
    data class Sending(val message: String, val timestamp: String) : Message()
}

sealed class ConversationScreenUIState {
    object Loading : ConversationScreenUIState()
    object Loaded : ConversationScreenUIState()
    object Empty : ConversationScreenUIState()
    data class Error(val errorMessage: String) : ConversationScreenUIState()
}

class ConversationScreenViewModelFactory(
    private val peerUserName: String,
    private val timestampFormatter: TimestampFormatter,
    private val chatRepository: ChatRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConversationScreenViewModel(peerUserName, timestampFormatter, chatRepository) as T
    }
}


class ConversationScreenViewModel(
    private val peerUserName: String,
    private val timestampFormatter: TimestampFormatter,
    private val messageRepository: ChatRepository
) : ViewModel() {

    init {
        loadMessages()
    }

    private val _uiState =
        MutableStateFlow<ConversationScreenUIState>(ConversationScreenUIState.Loading)
    val uiState: StateFlow<ConversationScreenUIState> = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(listOf())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private fun loadMessages() = viewModelScope.launch {

        _uiState.value = ConversationScreenUIState.Loading
        val messages = messageRepository.fetchMessages(peerUserName)

        if (messages.isEmpty()) {
            _uiState.value = ConversationScreenUIState.Empty
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

    fun sendMessage(message: String) = viewModelScope.launch {

        messageRepository.sendMessage(peerUserName, message).collect {

            val messageToSend =
                Message.Sending(it.message, timestampFormatter.format(it.iso8601Timestamp))

            when (it.status) {

                MessageStatus.QUEUED -> {
                    _uiState.value = ConversationScreenUIState.Loaded
                    _messages.value = _messages.value + messageToSend
                }

                MessageStatus.RECEIVED -> {

                }

                MessageStatus.SENT -> {
                    _uiState.value = ConversationScreenUIState.Loaded
                    _messages.value = _messages.value.map { msg ->
                        if (msg == messageToSend)
                            Message.Sent(messageToSend.message, messageToSend.timestamp)
                        else
                            msg
                    }
                }

            }

        }


    }

}