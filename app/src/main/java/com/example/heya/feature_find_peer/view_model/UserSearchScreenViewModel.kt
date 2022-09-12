package com.example.heya.feature_find_peer.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heya.feature_find_peer.repository.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class User(val imageURL: String, val userName: String)

sealed class UsersScreenUIState {
    object Empty : UsersScreenUIState()
    object FindingPreviouslyContacted : UsersScreenUIState()
    data class PreviousContactsFound(val contacts: List<User>) : UsersScreenUIState()
    object SearchingForUser : UsersScreenUIState()
    data class QueriedUserFound(val user: User) : UsersScreenUIState()
    data class QueriedUserNotFound(val searchQuery: String) : UsersScreenUIState()
}


@HiltViewModel
class UserSearchScreenViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UsersScreenUIState>(UsersScreenUIState.FindingPreviouslyContacted)

    val uiState: StateFlow<UsersScreenUIState> = _uiState

    private var _previousContacts: List<User>? = null

    init {
        fetchPreviousContacts()
    }

    fun findUser(userName: String) {

        _uiState.value = UsersScreenUIState.SearchingForUser

        viewModelScope.launch(Dispatchers.IO) {
            val user = usersRepository.findUser(userName)
            if (user == null) {
                _uiState.value = UsersScreenUIState.QueriedUserNotFound(userName)
            } else {
                _uiState.value =
                    UsersScreenUIState.QueriedUserFound(User(user.imageURL, user.userName))
            }
        }

    }

    fun fetchPreviousContacts() {

        if (_uiState.value is UsersScreenUIState.PreviousContactsFound) {
            return
        }

        if (_previousContacts != null) {
            _uiState.value = UsersScreenUIState.PreviousContactsFound(_previousContacts!!)
            return
        }

        _uiState.value = UsersScreenUIState.FindingPreviouslyContacted

        viewModelScope.launch(Dispatchers.IO) {

            val previousContacts = usersRepository.fetchAllPreviouslyContactedUsers()
            if (previousContacts.isEmpty()) {
                _uiState.value = UsersScreenUIState.Empty
            } else {
                _previousContacts = previousContacts.map { User(it.imageURL, it.userName) }
                _uiState.value = UsersScreenUIState.PreviousContactsFound(_previousContacts!!)
            }

        }
    }

}