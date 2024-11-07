package com.health.vita.ui.components.meals

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.health.vita.ui.theme.LightGray


@Composable
fun WaterIntakeTracker(totalWaterMl: Int, currentWaterMl: Int) {
    // Calcular el porcentaje del agua actual en relación al total
    val progress = (currentWaterMl.toFloat() / totalWaterMl.toFloat()).coerceIn(0f, 1f)
    val density = LocalDensity.current
    var containerHeight by remember {
        mutableStateOf(0.dp)
    }

    // Animar la altura de la caja interna basada en el progreso
    val animatedHeight by animateDpAsState(
        targetValue = containerHeight * progress,
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
    )

    // Animación para el movimiento de onda
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    // Caja contenedora
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .onGloballyPositioned { layoutCoordinates ->
                containerHeight = with(density) { layoutCoordinates.size.height.toDp() }
            }
            .clip(RoundedCornerShape(topStart = 70.dp, topEnd = 70.dp))
            .background(LightGray),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Caja interna que representa el nivel de agua
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(animatedHeight)
        ) {
            // Dibujar el agua con efecto de onda en el borde superior
            Canvas(modifier = Modifier.fillMaxSize()) {
                val waveHeight = if(progress < 1f && progress > 0f) 4.dp.toPx() else 0.dp.toPx() // Altura de la onda
                val waveFrequency = 40.dp.toPx() // Frecuencia de la onda

                // Crear un path para el agua con efecto de onda
                val waterPath = Path().apply {
                    // Mover al borde inferior izquierdo
                    moveTo(0f, size.height)
                    lineTo(0f, size.height - animatedHeight.toPx())

                    // Dibujar la onda en el borde superior del agua
                    for (x in 0..size.width.toInt() step 10) {
                        val y = size.height - animatedHeight.toPx() +
                                waveHeight * kotlin.math.sin((x + waveOffset).toRadians())
                        lineTo(x.toFloat(), y.toFloat())
                    }

                    // Cerrar el path en el borde inferior derecho
                    lineTo(size.width, size.height)
                    close()
                }

                // Rellenar el agua con un degradado
                drawPath(
                    path = waterPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF42A2FF), // Azul claro
                            Color(0xFF68A0CE)  // Azul oscuro
                        ),
                        startY = size.height - animatedHeight.toPx(),
                        endY = size.height
                    )
                )
            }
        }
    }
}

// Función auxiliar para convertir grados a radianes
private fun Float.toRadians() = this * (Math.PI / 180f).toFloat()

