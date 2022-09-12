package com.example.heya.core.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter


@Composable
fun HeyaCircleAvatar(imageURL: String, radiusAsFractionOfWidth: Float = 0.130f) {
    Image(
        painter = rememberAsyncImagePainter(model = imageURL),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth(radiusAsFractionOfWidth)
            .aspectRatio(1f)
            .clip(CircleShape)
    )
}