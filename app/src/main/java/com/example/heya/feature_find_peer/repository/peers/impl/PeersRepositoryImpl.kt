package com.example.heya.feature_find_peer.repository.peers.impl

import com.example.heya.core.data.db.MockDB
import com.example.heya.feature_find_peer.model.PeerModel
import com.example.heya.feature_find_peer.repository.peers.PeersRepository
import kotlinx.coroutines.delay

class PeersRepositoryImpl:PeersRepository {
    override suspend fun getLastMessageOfAllPeers(): List<PeerModel> {
        delay(2000)
        return MockDB.myChatBuddies.map { it.value }
    }
}