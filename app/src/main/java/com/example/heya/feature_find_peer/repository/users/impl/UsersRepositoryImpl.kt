package com.example.heya.feature_find_peer.repository.users.impl

import com.example.heya.core.data.db.MockDB
import com.example.heya.feature_find_peer.model.UserModel
import com.example.heya.feature_find_peer.repository.users.UsersRepository
import kotlinx.coroutines.delay

class UsersRepositoryImpl : UsersRepository {

    override suspend fun findUser(userName: String): UserModel? {
        delay(1000)
        return MockDB.allUsers[userName]
    }

    override suspend fun fetchAllPreviouslyContactedUsers(): List<UserModel> {
        delay(2000)
        return MockDB.previouslyContacted.toList()
    }

}