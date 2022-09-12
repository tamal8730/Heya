package com.example.heya.feature_chat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.heya.core.view.HeyaCircleAvatar
import com.example.heya.core.view.HeyaTextField
import com.example.heya.feature_chat.view_model.ConversationScreenUIState
import com.example.heya.feature_chat.view_model.ConversationScreenViewModel
import com.example.heya.feature_chat.view_model.Message

private enum class MessageType { SENT, RECEIVED, SENDING }

@Composable
fun ConversationScreen(
    peerUserName: String,
    peerPhotoURL: String,
    viewModel: ConversationScreenViewModel,
    onNavigateToBack: () -> Unit,
) {

    val uiState = viewModel.uiState.collectAsState().value
    val messages = viewModel.messages.collectAsState().value

    Scaffold(
        topBar = {
            Header(
                imageURL = peerPhotoURL,
                userName = peerUserName,
                onBack = onNavigateToBack
            )
        },
        bottomBar = {
            MessageEditText(PaddingValues(16.dp)) {
                viewModel.sendMessage(it)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {

            when (uiState) {

                is ConversationScreenUIState.Empty -> {
                    EmptyConversation(peerUserName = peerUserName)
                }

                is ConversationScreenUIState.Error -> TODO()

                is ConversationScreenUIState.Loaded -> {

                    val listState = rememberLazyListState()

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(innerPadding)
                            .fillMaxHeight(),
                        reverseLayout = true
                    ) {
                        items(messages.size) { index ->

                            when (val message = messages[messages.size - index - 1]) {

                                is Message.Received -> {
                                    ChatBubble(
                                        message = message.message,
                                        timestamp = message.timestamp,
                                        type = MessageType.RECEIVED
                                    )
                                }

                                is Message.Sent -> {
                                    ChatBubble(
                                        message = message.message,
                                        timestamp = message.timestamp,
                                        type = MessageType.SENT
                                    )
                                }

                                is Message.Sending -> {
                                    ChatBubble(
                                        message = message.message,
                                        timestamp = "sending...",
                                        type = MessageType.SENT
                                    )
                                }

                            }

                        }
                    }

                }

                is ConversationScreenUIState.Loading -> {
                    LoadingMessages()
                }

            }

        }
    }
}

@Composable
private fun LoadingMessages() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyConversation(peerUserName: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Start a conversation with $peerUserName. All messages are end-to-end encrypted. This means even we at heya cannot read your messages")
    }
}


@Composable
private fun Header(
    imageURL: String,
    userName: String,
    onBack: () -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        HeyaCircleAvatar(imageURL = imageURL)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = "@$userName", style = MaterialTheme.typography.h5)
    }
}


@Composable
private fun ChatBubble(message: String, timestamp: String, type: MessageType) {

    val color = when (type) {
        MessageType.SENT, MessageType.SENDING -> MaterialTheme.colors.primary
        MessageType.RECEIVED -> MaterialTheme.colors.surface
    }

    val shape = when (type) {
        MessageType.SENT, MessageType.SENDING -> RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
        MessageType.RECEIVED -> RoundedCornerShape(0.dp, 16.dp, 16.dp, 16.dp)
    }

    val alignment = when (type) {
        MessageType.SENT, MessageType.SENDING -> Alignment.End
        MessageType.RECEIVED -> Alignment.Start
    }

    val textColor = when (type) {
        MessageType.SENT, MessageType.SENDING -> MaterialTheme.colors.onPrimary
        MessageType.RECEIVED -> MaterialTheme.colors.onSurface
    }

    val subtext = when (type) {
        MessageType.SENT, MessageType.RECEIVED -> timestamp
        MessageType.SENDING -> "sending..."
    }

    Column(
        horizontalAlignment = alignment,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        BoxWithConstraints {
            Text(
                text = message,
                color = textColor,
                modifier = Modifier
                    .clip(shape)
                    .background(color = color)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .widthIn(0.dp, 0.5.dp.times(maxWidth.value))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtext,
            style = MaterialTheme.typography.caption.copy(
                color = MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.medium)
            )
        )
    }

}


@Composable
private fun MessageEditText(padding: PaddingValues, onSend: (message: String) -> Unit) {

    var text by remember { mutableStateOf("") }

    Box(modifier = Modifier.padding(padding)) {
        HeyaTextField(
            text = text,
            hint = "Message...",
            imeAction = ImeAction.Send,
            onValueChanged = { text = it },
            trailingLabel = "Send",
            onImeAction = { text = ""; onSend(it) },
        )
    }
}
