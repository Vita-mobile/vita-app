package com.health.vita.ui.components.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.health.vita.R

@Composable
fun CircularPhoto(
    @DrawableRes photo: Int,
    contentDescription: String = "",
    size: Int = 24
){
    Image(
        painter = painterResource(photo),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,            // crop the image if it's not a square
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)                       // clip to the circle shape
    )
}