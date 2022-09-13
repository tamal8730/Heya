package com.example.heya.core.data.db

import com.example.heya.feature_find_peer.model.PeerModel
import com.example.heya.core.model.MessageModel
import com.example.heya.feature_find_peer.model.UserModel
import java.util.*


object MockDB {

    const val myUserName = "tamal"
    const val myPassword = "12345678"

    val allUsers by lazy {
        mapOf(
            "surya" to UserModel(
                "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "surya"
            ),
            "yennefer" to UserModel(
                "https://images.pexels.com/photos/774095/pexels-photo-774095.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "yennefer",
            ),
            "kabir" to UserModel(
                "https://images.pexels.com/photos/1516680/pexels-photo-1516680.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "kabir",
            ),
            "manav" to UserModel(
                "https://images.pexels.com/photos/1139743/pexels-photo-1139743.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "manav",
            ),
            "adesh" to UserModel(
                "https://images.pexels.com/photos/1680175/pexels-photo-1680175.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                "adesh",
            ),
            "mahua" to UserModel(
                "https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg?auto=compress&cs=tinysrgb&w=600",
                "mahua"
            ),
            "aniruddh" to UserModel(
                "https://images.unsplash.com/photo-1568602471122-7832951cc4c5?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80",
                "aniruddh"
            ),
            "meenamma" to UserModel(
                "https://images.unsplash.com/photo-1542740348-39501cd6e2b4?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80",
                "meenamma"
            ),
        )
    }

    val previouslyContacted by lazy { mutableSetOf<UserModel>() }

    val myConversations by lazy { mutableMapOf<String, MutableList<MessageModel>>() }

    val messageQueue by lazy {
        mutableMapOf<String, Queue<MessageModel>>()
    }

    val myChatBuddies by lazy { mutableMapOf<String, PeerModel>() }

    fun updateLastMessage(peerUserName: String, newLastMessage: String, timestamp: String) {
        val imageURL = allUsers[peerUserName]?.imageURL ?: throw Exception("no such user")
        myChatBuddies[peerUserName] = PeerModel(
            peerUserName,
            imageURL,
            newLastMessage,
            timestamp,
        )
    }

}