package com.health.vita.main.presentation


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview

import com.health.vita.R
import com.health.vita.core.navigation.Screen
import com.health.vita.core.navigation.Screen.LOGIN
import com.health.vita.core.navigation.Screen.SIGN_UP
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.theme.VitaTheme
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(navController: NavController = rememberNavController()) {


    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.welcome_background),
            contentDescription = "Image background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                        startY = 0f,
                        endY = 300f
                    )
                )
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            Spacer(modifier = Modifier.weight(1f))


            Image(
                painter = painterResource(id = R.drawable.logo_v),
                contentDescription = "Logo de Vita",
                modifier = Modifier.size(90.dp)
            )


            Spacer(modifier = Modifier.height(23.dp))


            Text(
                text = "Bienvenido a Vita",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                ),
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(25.dp))


            Text(
                text = "Tu asistente personal de IA en fitness",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White
                ),
                textAlign = TextAlign.Center,
            )


            Spacer(modifier = Modifier.height(32.dp))

            PrimaryIconButton(

                modifier = Modifier.width(170.dp).height(55.dp),

                onClick = {

                    navController.navigate(SIGN_UP)

                },
                text = "Vamos, ve",
                arrow = true,
                color = MaterialTheme.colorScheme.secondary
            )


            Spacer(modifier = Modifier.height(40.dp))


            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "¿Ya tienes una cuenta? ",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White
                    ),

                )


                Text(
                    text = "Inicia sesión",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF26C8E0),
                        textDecoration = TextDecoration.Underline
                    ),

                    modifier = Modifier.clickable { navController.navigate(
                        LOGIN
                    ) }
                )
            }

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    VitaTheme {
        WelcomeScreen()
    }
}
