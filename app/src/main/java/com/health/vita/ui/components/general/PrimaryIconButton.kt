package com.health.vita.ui.components.general

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryIconButton(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .padding(start = 12.dp, end = 12.dp),
    onClick: () -> Unit = {},
    text: String = "Placeholder",
    arrow: Boolean = true,
    color: Color = MaterialTheme.colorScheme.primary,
    blackContent: Boolean = false,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(19.dp),
        colors = ButtonDefaults.buttonColors(
            color
        ),
        onClick = onClick,
        enabled = enabled
    ) {

        if(isLoading){

            CircularProgressIndicator(
            color = if (blackContent) Color.Black else Color.White,
            modifier = Modifier.size(24.dp)
            )

        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (blackContent) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary
            )
            Icon(
                if (arrow) Icons.AutoMirrored.Rounded.ArrowForward else Icons.Outlined.Lock,
                modifier = Modifier
                    .size(24.dp),
                tint = if (blackContent) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background,
                contentDescription = ""
            )
        }
    }
}