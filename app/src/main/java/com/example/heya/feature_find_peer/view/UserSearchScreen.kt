package com.example.heya.feature_find_peer.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.heya.core.view.HeyaCircleAvatar
import com.example.heya.core.view.HeyaTextField
import com.example.heya.core.view.HeyaTopAppBar
import com.example.heya.feature_find_peer.view_model.User
import com.example.heya.feature_find_peer.view_model.UserSearchScreenViewModel
import com.example.heya.feature_find_peer.view_model.UsersScreenUIState


@Composable
fun UserFinderScreen(
    viewModel: UserSearchScreenViewModel,
    onNavigateToConversation: (peerUserName: String, peerImageURL: String) -> Unit,
) {

    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(topBar = { HeyaTopAppBar(title = "new message") }) {
        Column(modifier = Modifier.padding(it)) {
            Spacer(modifier = Modifier.height(16.dp))

            SearchBar(
                { query -> viewModel.findUser(query) },
                { viewModel.fetchPreviousContacts() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {

                is UsersScreenUIState.Empty -> {

                }

                is UsersScreenUIState.FindingPreviouslyContacted -> {
                    Loading()
                }

                is UsersScreenUIState.PreviousContactsFound -> {
                    UsersList(
                        heading = "Previously contacted",
                        uiState.contacts
                    ) { user -> onNavigateToConversation(user.userName, user.imageURL) }
                }

                is UsersScreenUIState.QueriedUserFound -> {
                    UsersList(
                        heading = "Found user",
                        listOf(uiState.user),
                    ) { user -> onNavigateToConversation(user.userName, user.imageURL) }
                }

                is UsersScreenUIState.QueriedUserNotFound -> {
                    NoSearchResult(query = uiState.searchQuery)
                }

                is UsersScreenUIState.SearchingForUser -> {
                    Loading()
                }


            }

        }
    }
}

@Composable
private fun NoSearchResult(query: String) {
    Text(
        text = "No results found for $query",
        style = MaterialTheme.typography.subtitle2.copy(
            color = MaterialTheme.colors.onBackground.copy(
                alpha = ContentAlpha.medium
            )
        ),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun Loading() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator()
    }
}

@Composable
private fun UsersList(heading: String, users: List<User>, onClick: (user: User) -> Unit) {
    Column {
        Text(
            text = heading,
            style = MaterialTheme.typography.subtitle2.copy(
                color = MaterialTheme.colors.onBackground.copy(
                    alpha = ContentAlpha.medium
                )
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn {
            items(users.size) { index ->
                UserTile(
                    imageURL = users[index].imageURL,
                    userName = users[index].userName,
                    onClick = { onClick(users[index]) }
                )
            }
        }
    }
}

@Composable
private fun SearchBar(onSearchStart: (String) -> Unit, onQueryCleared: () -> Unit) {

    var text by remember { mutableStateOf("") }

    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        HeyaTextField(
            text = text,
            hint = "Search...",
            onValueChanged = {
                text = it
                if (it.isEmpty()) {
                    onQueryCleared()
                }
            },
            roundedCornerPercentage = 10,
            singleLine = true,
            imeAction = ImeAction.Search,
            onImeAction = { query ->
                text = query.trim(); if (text.isNotBlank()) onSearchStart(query.trim())
            },
            trailingLabel = "Search",
            dismissKeyboardOnImeAction = true,
        )
    }
}

@Composable
private fun UserTile(imageURL: String, userName: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            HeyaCircleAvatar(imageURL = imageURL)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = userName)
        }
    }
}