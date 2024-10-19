package com.health.vita.main.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.health.vita.R
import com.health.vita.ui.theme.LightTurquoise
import com.health.vita.ui.theme.VitaTheme

@Composable
fun WelcomeScreen(navController: NavController = rememberNavController()) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo sin márgenes
        Image(
            painter = painterResource(id = R.drawable.welcome_background), // Cambia esto por el nombre de tu imagen
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(), // Asegura que cubra todo el fondo
            contentScale = ContentScale.Crop // Escala la imagen para cubrir todo sin márgenes
        )

        // Sombreado difuminado en la parte inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp) // Ajusta la altura del sombreado
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)), // Degradado de transparente a negro
                        startY = 0f,
                        endY = 300f // Ajusta esta distancia para controlar el difuminado
                    )
                )
        )

        // Contenedor para los elementos centrados desde la mitad hacia abajo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp) // Ajusta la separación horizontal
                .align(Alignment.BottomCenter), // Alinea la columna en la parte inferior
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom // Coloca los elementos en la parte inferior de la columna
        ) {
            // Espaciador para empujar los elementos hacia arriba
            Spacer(modifier = Modifier.weight(1f)) // Controla la distancia desde el borde inferior

            // Logo más pequeño
            Image(
                painter = painterResource(id = R.drawable.logo_v),
                contentDescription = "Logo de Vita",
                modifier = Modifier.size(90.dp) // Tamaño ajustado del logo
            )

            // Espaciado entre el logo y el texto
            Spacer(modifier = Modifier.height(23.dp))

            // Texto de bienvenida en bold
            Text(
                text = "Bienvenido a Vita",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold // Texto en bold
                ),
                textAlign = TextAlign.Center,
                fontSize = 30.sp // Tamaño de fuente
            )

            // Espaciado ajustable entre los textos
            Spacer(modifier = Modifier.height(25.dp))

            // Texto auxiliar
            Text(
                text = "Tu asistente personal de IA en fitness",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White
                ),
                textAlign = TextAlign.Center,
                fontSize = 15.sp // Tamaño de fuente
            )

            // Espaciado entre el texto y el botón
            Spacer(modifier = Modifier.height(32.dp))

            // Botón con color Aquamarine y texto medium bold con ícono
            Button(
                onClick = { navController.navigate("HOME") },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightTurquoise),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .padding(horizontal = 80.dp) // Ajusta el ancho del botón
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Vamos, ve",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold // Texto medium bold
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Espacio entre texto y ícono
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Flecha hacia adelante",
                        tint = Color.White // Ícono de color blanco
                    )
                }
            }

            // Espaciado entre el botón y el texto de hipervínculo
            Spacer(modifier = Modifier.height(40.dp)) // Ajusta el espacio según sea necesario

            // Texto de hipervínculo con los colores requeridos
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Texto de "¿Ya tienes una cuenta?"
                Text(
                    text = "¿Ya tienes una cuenta? ",
                    color = Color.White, // Texto en blanco
                    textAlign = TextAlign.Center
                )

                // Texto de "Inicia sesión" con hipervínculo
                Text(
                    text = "Inicia sesión",
                    color = LightTurquoise, // Mismo color que el botón
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { navController.navigate("LoginScreen") }
                )
            }

            // Espaciador inferior para separar del borde inferior
            Spacer(modifier = Modifier.height(64.dp)) // Ajusta esta distancia según tu imagen de fondo
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
