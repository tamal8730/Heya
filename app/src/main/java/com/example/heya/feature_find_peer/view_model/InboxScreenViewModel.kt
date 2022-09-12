package com.example.heya.feature_find_peer.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heya.feature_find_peer.repository.peers.PeersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
    data class Loaded(val buddies: List<ChatBuddy>) : InboxScreenUIState()
    object Loading : InboxScreenUIState()
}

class InboxScreenViewModel(
    private val peersRepository: PeersRepository
) : ViewModel() {

    init {
        loadAllConversations()
    }

    private val _uiState = MutableStateFlow<InboxScreenUIState>(InboxScreenUIState.Loading)
    val uiState: StateFlow<InboxScreenUIState> = _uiState.asStateFlow()


    private fun loadAllConversations() = viewModelScope.launch {
//        _uiState.value = InboxScreenUIState.Loading
        val allChatBuddies = peersRepository.getLastMessageOfAllPeers()
        if (allChatBuddies.isEmpty()) {
            _uiState.value = InboxScreenUIState.Empty
        } else {
            _uiState.value = InboxScreenUIState.Loaded(
                allChatBuddies.map {
                    ChatBuddy(
                        userName = it.userName,
                        imageURL = it.imageURL,
                        lastMessage = it.lastMessage,
                        timestamp = it.lastMessageTimestamp,
                        unreadCount = it.unreadMessageCount,
                    )
                }
            )
        }
    }

}