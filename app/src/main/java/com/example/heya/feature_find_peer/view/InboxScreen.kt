package com.example.heya.feature_find_peer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.heya.core.view.HeyaCircleAvatar
import com.example.heya.core.view.HeyaTextField
import com.example.heya.core.view.HeyaTopAppBar
import com.example.heya.feature_find_peer.view_model.InboxScreenUIState
import com.example.heya.feature_find_peer.view_model.InboxScreenViewModel

@Composable
fun InboxScreen(
    viewModel: InboxScreenViewModel,
    onNavigateToUserSearch: () -> Unit,
    onNavigateToConversation: (peerUserName: String, imageURL: String) -> Unit
) {

    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        topBar = { HeyaTopAppBar(title = "heya") },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToUserSearch) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "New conversation"
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Spacer(modifier = Modifier.height(16.dp))
            SearchBar()
            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {

                is InboxScreenUIState.Empty -> {
                    EmptyInbox()
                }

                is InboxScreenUIState.Error -> {

                }

                is InboxScreenUIState.Loaded -> {

                    val buddies = viewModel.chatBuddies.collectAsState().value

                    LazyColumn(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(it)
                            .fillMaxHeight()
                    ) {

                        items(buddies.size) { index ->
                            Peer(
                                imageURL = buddies[index].imageURL,
                                userName = buddies[index].userName,
                                lastMessage = buddies[index].lastMessage,
                                timestamp = buddies[index].timestamp,
                                unreadCount = buddies[index].unreadCount,
                                onClick = {
                                    onNavigateToConversation(
                                        buddies[index].userName,
                                        buddies[index].imageURL
                                    )
                                }
                            )
                        }
                    }

                }

                is InboxScreenUIState.Loading -> {
                    LoadingChats()
                }

            }

        }
    }
}

@Composable
private fun EmptyInbox() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Start a conversation with someone by tapping the button below")
    }
}

@Composable
private fun LoadingChats() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SearchBar() {

    var text by remember { mutableStateOf("") }

    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        HeyaTextField(
            text = text,
            hint = "Search...",
            onValueChanged = { text = it },
            roundedCornerPercentage = 10,
        )
    }
}

@Composable
private fun Peer(
    imageURL: String,
    userName: String,
    lastMessage: String,
    timestamp: String,
    unreadCount: Int = 0,
    onClick: () -> Unit
) {

    Box(modifier = Modifier
        .clickable { onClick() }
        .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HeyaCircleAvatar(imageURL = imageURL, radiusAsFractionOfWidth = 0.155f)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(10f)) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = lastMessage,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                    style = if (unreadCount == 0) {
                        MaterialTheme.typography.body2.copy(
                            color = MaterialTheme.colors.onBackground.copy(
                                alpha = ContentAlpha.medium
                            )
                        )
                    } else {
                        MaterialTheme.typography.body2.copy(
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = timestamp,
                    style = MaterialTheme.typography.caption.copy(
                        color = MaterialTheme.colors.onBackground.copy(
                            alpha = ContentAlpha.medium
                        )
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                UnreadMessagesCounter(unreadCount)
            }
        }
    }

}

@Composable
private fun UnreadMessagesCounter(count: Int = 0) {
    if (count <= 0) return
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(40)
            )
            .padding(4.dp)
            .fillMaxWidth(0.051f)
            .aspectRatio(1f)
    ) {
        Text(
            text = if (count in 1..99) count.toString() else "99+",
            style = MaterialTheme.typography.caption.copy(
                color = MaterialTheme.colors.onPrimary,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }

}