package com.example.heya.core.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun HeyaTopAppBar(title: String) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        title = { Text(title, style = MaterialTheme.typography.h5) },
        modifier = Modifier.padding(top = 8.dp)
    )
}