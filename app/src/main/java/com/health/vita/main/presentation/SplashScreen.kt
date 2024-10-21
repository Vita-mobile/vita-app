package com.health.vita.main.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.health.vita.R
import com.health.vita.ui.theme.VitaTheme

@Composable
fun SplashScreen(navController: NavController = rememberNavController()) {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.vita_icon),
                contentDescription = "Logo de Vita",
                modifier = Modifier
                    .size(150.dp)
                    .aspectRatio(0.4f),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Vita",
                style =  MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.W800
                )
            )

            Spacer(modifier = Modifier.height(30.dp))


            Text(
                text = "Tu asistente personal de IA en fitness",
                style =  MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.background
                )
            )

            Spacer(modifier = Modifier.height(100.dp))

            CircularProgressIndicator(modifier = Modifier.size(80.dp),
                color = MaterialTheme.colorScheme.background,
                strokeWidth = 10.dp )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    VitaTheme {
        SplashScreen()
    }
}
