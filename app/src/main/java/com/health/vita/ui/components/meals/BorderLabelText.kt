package com.health.vita.ui.components.meals

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.health.vita.ui.theme.MintGreen

@Composable
fun BorderLabelText(
    modifier: Modifier = Modifier,
    text: String = "Placeholder",
    color: Color = MintGreen,
    border: Boolean = true,
    icon: ImageVector? = null,
    onIconClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(Color(0xFFF4F4F4), shape = RoundedCornerShape(28.dp))
            .then(
                if (border) Modifier.border(
                    2.dp, color, shape = RoundedCornerShape(28.dp)
                ) else Modifier
            )
            .padding(16.dp)
            , contentAlignment = Alignment.Center
    ) {

        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                textAlign = TextAlign.Center,
            )
            if (icon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    icon,
                    contentDescription = "Icon",
                    modifier = Modifier
                        .clickable { onIconClick() }
                )
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}