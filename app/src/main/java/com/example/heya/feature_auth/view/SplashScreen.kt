package com.example.heya.feature_auth.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.heya.feature_auth.view_model.LoginEvent
import com.example.heya.feature_auth.view_model.SplashScreenUIState
import com.example.heya.feature_auth.view_model.SplashScreenViewModel

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToPeers: () -> Unit,
) {

    LaunchedEffect(key1 = LocalContext.current) {
        viewModel.loginEvent.collect { event ->
            when (event) {
                LoginEvent.LoggedIn -> onNavigateToPeers()
                LoginEvent.LoggedOut -> onNavigateToLogin()
            }
        }
    }

    Scaffold {
        Box(
            modifier = Modifier
                .padding(it)
                .background(color = MaterialTheme.colors.primary)
                .fillMaxSize()
        ) {

            when (val state = viewModel.uiState.collectAsState().value) {

                is SplashScreenUIState.Empty -> {
                    Text(
                        text = "heya",
                        style = MaterialTheme.typography.h2,
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }
                is SplashScreenUIState.Error -> {
                    Text(
                        text = state.errorMessage,
                        style = MaterialTheme.typography.h2,
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }

            }

        }
    }

}