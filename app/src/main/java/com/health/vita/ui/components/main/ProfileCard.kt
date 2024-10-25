package com.health.vita.ui.components.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.health.vita.R

@Preview
@Composable
fun ProfileCard() {
    Row(
        Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            Modifier
                .weight(2f)
                .padding(horizontal = 12.dp),         verticalAlignment = Alignment.CenterVertically,
        ){
            CircularPhoto(photo = R.drawable.male_image, "profile picture", 78)
            Column(Modifier.padding(horizontal = 12.dp)) {
                Text(
                    text = "Alejandro",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.background
                )
                Text(
                    text = "Jun 25, 2024",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.weight(1f)
        ){
            CircularIconOutlinedIconButton(
                icon = R.drawable.baseline_notifications_24,
                color = MaterialTheme.colorScheme.background
            )
            CircularIconOutlinedIconButton(
                icon = R.drawable.baseline_settings_24,
                color = MaterialTheme.colorScheme.background
            )
        }

    }
}

