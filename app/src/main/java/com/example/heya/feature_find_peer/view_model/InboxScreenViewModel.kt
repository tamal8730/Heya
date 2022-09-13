package com.example.heya.feature_find_peer.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heya.core.message_listener.MessageListener
import com.example.heya.feature_find_peer.repository.peers.PeersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatBuddy(
    val userName: String,
    val imageURL: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0
)

sealed class InboxScreenUIState {
    object Empty : InboxScreenUIState()
    data class Error(val errorMessage: String) : InboxScreenUIState()
    object Loaded : InboxScreenUIState()
    object Loading : InboxScreenUIState()
}


@HiltViewModel
class InboxScreenViewModel @Inject constructor(
    private val peersRepository: PeersRepository,
    private val messageListener: MessageListener,
) : ViewModel() {

    private val _chatBuddies = MutableStateFlow<List<ChatBuddy>>(listOf())
    val chatBuddies: StateFlow<List<ChatBuddy>> = _chatBuddies.asStateFlow()

    init {
        loadAllConversations()
        startListeningToMessages()
    }

    private val _uiState = MutableStateFlow<InboxScreenUIState>(InboxScreenUIState.Loading)
    val uiState: StateFlow<InboxScreenUIState> = _uiState.asStateFlow()

    private fun startListeningToMessages() = viewModelScope.launch {
        messageListener.addLastMessageListener {
            loadAllConversations()
        }
    }

    private fun loadAllConversations() = viewModelScope.launch {
//        _uiState.value = InboxScreenUIState.Loading
        val allChatBuddies = peersRepository.getLastMessageOfAllPeers()
        if (allChatBuddies.isEmpty()) {
            _uiState.value = InboxScreenUIState.Empty
        } else {
            _uiState.value = InboxScreenUIState.Loaded
            _chatBuddies.value = allChatBuddies.map {
                ChatBuddy(
                    userName = it.userName,
                    imageURL = it.imageURL,
                    lastMessage = it.lastMessage,
                    timestamp = it.lastMessageTimestamp,
                    unreadCount = it.unreadMessageCount,
                )
            }
        }
    }

}