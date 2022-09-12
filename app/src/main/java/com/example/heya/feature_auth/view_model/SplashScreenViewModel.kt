package com.example.heya.feature_auth.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heya.feature_auth.repository.auth.AuthRepository
import com.example.heya.feature_auth.repository.auth.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashScreenUIState {
    object Empty : SplashScreenUIState()
    data class Error(val errorMessage: String) : SplashScreenUIState()
}

sealed class LoginEvent {
    object LoggedIn : LoginEvent()
    object LoggedOut : LoginEvent()
}


@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashScreenUIState>(SplashScreenUIState.Empty)
    val uiState: StateFlow<SplashScreenUIState> = _uiState

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent: SharedFlow<LoginEvent> = _loginEvent

    init {
        viewModelScope.launch(Dispatchers.IO) { tryLogin() }
    }

    private suspend fun tryLogin() {

        val loginCredentials = authRepository.getLoginCredentials()
        if (loginCredentials == null) {
            _loginEvent.emit(LoginEvent.LoggedOut)
        } else {
            when (authRepository.login(loginCredentials)) {
                LoginResponse.Error -> {
                    _uiState.value = SplashScreenUIState.Error("error")
                }
                LoginResponse.NoAccount -> _loginEvent.emit(LoginEvent.LoggedOut)
                LoginResponse.Success -> _loginEvent.emit(LoginEvent.LoggedIn)
            }
        }

    }

}