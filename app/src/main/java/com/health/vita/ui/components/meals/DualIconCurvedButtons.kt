package com.health.vita.ui.components.meals

import android.media.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.health.vita.ui.theme.MintGreen

@Composable
fun DualIconCurvedButtons(
    modifier: Modifier = Modifier,
    bgColor: Color = MintGreen,
    borderColor: Color = MintGreen,
    border: Boolean = false,
    borderWidth: Int = 2,
    leftIcon: ImageVector = Icons.Outlined.KeyboardArrowDown,
    rightIcon: ImageVector = Icons.Outlined.KeyboardArrowUp,
    leftIconOnClick: () -> Unit = {},
    rightIconOnClick: () -> Unit = {},
    radius: Int = 28
) {
    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = radius.dp, bottomStart = radius.dp))
                .then(
                    if (border) Modifier.border(
                        borderWidth.dp, borderColor, shape = RoundedCornerShape(topStart = radius.dp, bottomStart = radius.dp)
                    ) else Modifier
                )
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = leftIconOnClick) {
                Icon(
                    imageVector = leftIcon,
                    contentDescription = "Left Icon"
                )
            }
        }
        Spacer(modifier = Modifier.width(1.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .clip(RoundedCornerShape(topEnd = radius.dp, bottomEnd = radius.dp))
                .then(
                    if (border) Modifier.border(
                        borderWidth.dp, borderColor, shape = RoundedCornerShape(topEnd = radius.dp, bottomEnd = radius.dp)
                    ) else Modifier
                )
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = rightIconOnClick) {
                Icon(
                    imageVector = rightIcon,
                    contentDescription = "Right Icon"
                )
            }
        }
    }
}