package com.health.vita.ui.components.general

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.health.vita.R
import com.health.vita.main.presentation.AccountSettingsScreen
import com.health.vita.ui.theme.DarkBlue
import com.health.vita.ui.theme.MediumGray
import com.health.vita.ui.theme.TranslucentBlue
import com.health.vita.ui.theme.VitaTheme

@Composable
fun GeneralTopBar(
    modifier: Modifier = Modifier,
    text: String = "Placeholder",
    step: Int = 1,
    total: Int = 1,
    onClick: () -> Unit,
    onClickIcon: () -> Unit = {},
    lightMode: Boolean = true,
    hasStep: Boolean = true,
    hasIcon: Boolean = false
) {

    Row(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp)
                .height(48.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
                    .clip(CircleShape)
                    .border(1.dp, if (lightMode) MediumGray else Color(0x66FFFFFF), CircleShape)
            ) {
                IconButton(
                    onClick = onClick
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        modifier = Modifier
                            .size(24.dp),
                        tint = if (lightMode) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background,
                        contentDescription = stringResource(id = R.string.shopping_cart_content_desc)
                    )
                }
            }
            if (hasIcon || hasStep) {
                Box(modifier = Modifier.weight(1f))
            } else {
                Box(modifier = Modifier.weight(0.5f))
            }
            Text(
                text = text,
                style = if (lightMode) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 26.sp
                ),
                color = if (lightMode) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background
            )
            Box(modifier = Modifier.weight(1f))

            if (hasStep) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(TranslucentBlue)
                        .wrapContentSize(Alignment.Center)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "$step de $total",
                        style = MaterialTheme.typography.labelMedium,
                        color = DarkBlue,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            if (hasIcon) {

                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .clip(CircleShape)
                        .border(1.dp, if (lightMode) MediumGray else Color(0x66FFFFFF), CircleShape)

                ) {
                    IconButton(
                        onClick = onClickIcon
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.round_more_horiz_24),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = if (lightMode) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background

                        )
                    }


                }


            }


        }
    }
}


