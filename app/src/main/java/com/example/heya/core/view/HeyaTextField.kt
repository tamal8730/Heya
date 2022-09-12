package com.example.heya.core.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

enum class HeyaTextFieldState {
    BUSY, ENABLED
}

@Composable
fun HeyaTextField(
    text: String,
    hint: String,
    backgroundColor: Color = MaterialTheme.colors.surface,
    imeAction: ImeAction = ImeAction.Default,
    singleLine: Boolean = false,
    roundedCornerPercentage: Int = 50,
    state: HeyaTextFieldState = HeyaTextFieldState.ENABLED,
    onValueChanged: (String) -> Unit,
    onImeAction: ((String) -> Unit)? = null,
    dismissKeyboardOnImeAction: Boolean = false,
    trailingLabel: String? = null,
) {

    val focusManager = LocalFocusManager.current

    Surface(shape = RoundedCornerShape(roundedCornerPercentage), color = backgroundColor) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                )
        ) {
            if (text.isEmpty()) {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)
                    )
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = text,
                    onValueChange = { onValueChanged(it) },
                    modifier = Modifier.weight(1f),
                    singleLine = singleLine,
                    maxLines = if (singleLine) 1 else 5,
                    textStyle = MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.onSurface
                    ),
                    cursorBrush = Brush.verticalGradient(
                        0.00f to MaterialTheme.colors.primary,
                        1.00f to MaterialTheme.colors.primary
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = imeAction),
                    keyboardActions = KeyboardActions {
                        if (text.isNotEmpty() && onImeAction != null) {

                            onImeAction(text)
                            if (dismissKeyboardOnImeAction) {
                                focusManager.clearFocus()
                            }

                        }
                    }
                )
                if (trailingLabel != null && text.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = trailingLabel,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.clickable {
                            if (onImeAction != null) {

                                onImeAction(text)
                                if (dismissKeyboardOnImeAction) {
                                    focusManager.clearFocus()
                                }

                            }

                        }
                    )
                }
            }
        }
    }
}