package com.example.heya.feature_auth.repository.auth.impl

import com.example.heya.core.data.db.MockDB
import com.example.heya.feature_auth.repository.auth.AuthRepository
import com.example.heya.feature_auth.repository.auth.LoginCredentials
import com.example.heya.feature_auth.repository.auth.LoginResponse
import kotlinx.coroutines.delay


class AuthRepositoryImpl : AuthRepository {

    override suspend fun login(loginCredentials: LoginCredentials): LoginResponse {
        delay(1000)
        return LoginResponse.Success
    }

    override suspend fun getLoginCredentials(): LoginCredentials? {
        delay(1000)
        return LoginCredentials(MockDB.myUserName, MockDB.myPassword)
    }

}