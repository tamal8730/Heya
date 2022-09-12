package com.example.heya.feature_find_peer.repository.users

import com.example.heya.feature_find_peer.model.UserModel

interface UsersRepository {

    suspend fun findUser(userName: String): UserModel?

    suspend fun fetchAllPreviouslyContactedUsers(): List<UserModel>

}