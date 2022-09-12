package com.example.heya.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.heya.feature_auth.view.SplashScreen
import com.example.heya.feature_auth.view_model.SplashScreenViewModel
import com.example.heya.feature_chat.view.ConversationScreen
import com.example.heya.feature_chat.view_model.ConversationScreenViewModel
import com.example.heya.feature_find_peer.view.InboxScreen
import com.example.heya.feature_find_peer.view.UserFinderScreen
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(route = Screen.Splash.route) {
            SplashScreen(
                viewModel = hiltViewModel<SplashScreenViewModel>(),
                onNavigateToPeers = {
                    navController.navigate(Screen.Peers.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {}
            )
        }

        composable(route = Screen.Peers.route) {
            InboxScreen(
                viewModel = hiltViewModel(),
                onNavigateToUserSearch = {
                    navController.navigate(Screen.UserSearch.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToConversation = { peerUserName, imageURL ->
                    navController.navigate(
                        Screen.Conversation.getFullPath(
                            peerUserName,
                            URLEncoder.encode(imageURL, "UTF-8")
                        )
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Screen.UserSearch.route) {
            UserFinderScreen(
                viewModel = hiltViewModel(),
                onNavigateToConversation = { peerUserName, peerImageURL ->
                    navController.navigate(
                        Screen.Conversation.getFullPath(
                            peerUserName,
                            URLEncoder.encode(peerImageURL, "UTF-8")
                        )
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Screen.Conversation.route,
            arguments = listOf(
                navArgument(Screen.Conversation.argUserName) { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val peerUserName = backStackEntry.arguments?.getString(Screen.Conversation.argUserName)
            val peerImageURL = backStackEntry.arguments?.getString(Screen.Conversation.argImageURL)
            // TODO: handle the case when arguments are null
            ConversationScreen(
                viewModel = hiltViewModel(),
                peerUserName = peerUserName!!,
                peerPhotoURL = URLDecoder.decode(peerImageURL, "UTF-8"),
                onNavigateToBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}