package com.health.vita.ui.components.meals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.health.vita.ui.theme.MintGreen

@Composable
fun BorderLabelText(
    modifier: Modifier = Modifier,
    text: String = "Placeholder",
    color: Color = Color(0xFF98FB98),
    border: Boolean = true
) {
    Box(
        modifier = modifier
            .background(Color(0xFFF4F4F4), shape = RoundedCornerShape(20.dp))
            .then(
                if (border) Modifier.border(
                    2.dp, color, shape = RoundedCornerShape(20.dp)
                ) else Modifier
            )
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}