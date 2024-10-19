package com.health.vita.main.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
        .background(MaterialTheme.colorScheme.primary)) { // Fondo MainBlue

        // Contenedor para centrar el logo y el texto
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Centra el contenido verticalmente
        ) {
            // Imagen del logo
            Image(
                painter = painterResource(id = R.drawable.logo_vita), // Cambia esto por el nombre de tu logo
                contentDescription = "Logo de Vita",
                modifier = Modifier
                    .size(200.dp) // Ajusta el tamaño según sea necesario
                    .aspectRatio(0.4f), // Mantiene la relación de aspecto
                contentScale = ContentScale.Fit // Cambia a Fit para ajustar sin recortes
            )

            // Espaciado entre el logo y el texto
            Spacer(modifier = Modifier.height(40.dp))

            // Texto debajo del logo
            Text(
                text = "Tu asistente personal de IA en fitness",
                color = Color.White,
                fontSize = 18.sp,
                //fontWeight = FontWeight.SemiBold, // Texto en semibold
                textAlign = TextAlign.Center
            )
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
