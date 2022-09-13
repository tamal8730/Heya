package com.example.heya.feature_find_peer.repository.peers

import com.example.heya.feature_find_peer.model.PeerModel

interface PeersRepository {
    suspend fun getLastMessageOfAllPeers(): List<PeerModel>

}