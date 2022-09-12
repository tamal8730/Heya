package com.example.heya.feature_auth.repository.auth

data class LoginCredentials(val userName: String, val password: String)

sealed class LoginResponse {
    object Success : LoginResponse()
    object NoAccount : LoginResponse()
    object Error : LoginResponse()
}

interface AuthRepository {

    suspend fun login(loginCredentials: LoginCredentials): LoginResponse

    suspend fun getLoginCredentials(): LoginCredentials?

}