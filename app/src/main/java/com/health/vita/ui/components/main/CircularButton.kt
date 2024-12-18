package com.health.vita.ui.components.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.health.vita.R


@Composable
fun CircularIconOutlinedIconButton(
    @DrawableRes icon: Int,
    color: Color = Color.White,
    size: Int = 56,
    containerColor: Color = Color.Transparent,
    border: BorderStroke? = BorderStroke(2.dp, color),
    onClick: () -> Unit,
    ) {
    OutlinedIconButton(
        onClick =  onClick ,
        modifier = Modifier.size(size.dp),
        colors = IconButtonDefaults.outlinedIconButtonColors(
            containerColor = containerColor,
            contentColor = color
        ),
        border = border, // Cambia el color del borde
    ) {
        Icon(painter = painterResource(id = icon), contentDescription = "Home", modifier = Modifier.size((size*0.43f).dp))
    }
}

@Preview(showBackground = false)
@Composable
fun prev(){
    CircularIconOutlinedIconButton(icon = R.drawable.baseline_notifications_24){}
}