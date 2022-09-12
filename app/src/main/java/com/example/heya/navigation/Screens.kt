package com.example.heya.navigation

sealed class Screen(val route: String) {

    object Splash : Screen("splash_screen")
    object Peers : Screen("peers_screen")

    object Conversation : Screen("conversation_screen/{user_name}/{image_url}") {

        val argUserName = "user_name"
        val argImageURL = "image_url"

        fun getFullPath(userName: String, imageURL: String): String {
            return "conversation_screen/$userName/$imageURL"
        }

    }

    object UserSearch : Screen("user_search_screen")

}
