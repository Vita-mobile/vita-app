package com.health.vita.ui.components.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.health.vita.R
import com.health.vita.ui.theme.VitaTheme


@Composable
fun CardWithTitle(
    title: String,
    @DrawableRes background: Int,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Column(Modifier.background(MaterialTheme.colorScheme.background).padding(vertical = 10.dp)) {
        Text(text = title, style = MaterialTheme.typography.bodyMedium)
        Card(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(top = 20.dp)
        ) {
            Box(contentAlignment = Alignment.BottomStart, modifier = Modifier.clickable { onClick() }){
                Image(
                    painter = painterResource(id = background),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                content()
            }
        }

    }
}

@Preview()
@Composable
fun prevCard(){
    VitaTheme {
        CardWithTitle("Entrenamientos", R.drawable.female_image) {
            Text(
                text = "Proximamente",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}
