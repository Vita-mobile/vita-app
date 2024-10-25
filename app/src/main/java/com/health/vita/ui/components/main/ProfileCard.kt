package com.health.vita.ui.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.health.vita.R
import com.health.vita.ui.theme.VitaTheme
import java.text.SimpleDateFormat
import java.util.Locale
val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale("es"))

@Composable
fun ProfileCard(
    name: String = "John",
    date: Timestamp = Timestamp.now(),
    onClick: () -> Unit
) {
    Column(){
        Row(
            Modifier

                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = 18.dp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround

        ) {
            Row(
                Modifier
                    .weight(2f)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularPhoto(photo = R.drawable.male_image, "profile picture", 78)
                Column(Modifier.padding(horizontal = 12.dp)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.background
                    )
                    Text(
                        text = dateFormat.format(date.toDate()),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.weight(1.2f).padding(horizontal = 16.dp)
            ) {
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
        Box(
            modifier = Modifier
                .height(30.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                )
        )
    }
}

@Preview(showBackground = false)
@Composable
fun PrevProfCard(){
    VitaTheme {
        ProfileCard(){

        }
    }
}