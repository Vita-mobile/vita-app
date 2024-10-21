package com.health.vita.ui.components.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.vita.ui.theme.DarkBlue
import com.health.vita.ui.theme.LightGray
import com.health.vita.ui.theme.MainBlue
import com.health.vita.ui.theme.MediumGray
import com.health.vita.ui.theme.TranslucentBlue

@Composable
fun WeeklyBarChart(
    data: List<Pair<String, Float>>,
    currentDay: String
) {
    var selectedValue by remember { mutableStateOf(-1f) }
    var selectedDay by remember { mutableStateOf("") }

    val maxValue = data.maxOf { it.second }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Progreso de la semana",
                style = MaterialTheme.typography.headlineSmall,
                color = DarkBlue
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Eje Y
            Column(
                modifier = Modifier
                    .width(25.dp),
                horizontalAlignment = Alignment.End
            ) {
                for (i in 5 downTo 0) {
                    val yValue = i * 20
                    Text(
                        text = yValue.toString(),
                        fontSize = 12.sp,
                        color = MediumGray,
                        modifier = Modifier.padding(vertical = 7.5.dp)
                    )
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Canvas(
                    modifier = Modifier.fillMaxWidth().height(220.dp),
                    onDraw = {
                        drawLine(
                            color = LightGray,
                            start = Offset(40f, 0f),
                            end = Offset(40f, size.height),
                            strokeWidth = 4f
                        )

                        for (i in 0..5) {
                            val yPosition = size.height - (i * (size.height / 5))
                            drawLine(
                                color = LightGray.copy(alpha = 0.5f),
                                start = Offset(40f, yPosition),
                                end = Offset(size.width, yPosition),
                                strokeWidth = 5f
                            )
                        }
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    data.forEach { (day, value) ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            selectedValue = value
                                            selectedDay = day
                                            tryAwaitRelease()
                                            selectedValue = -1f
                                            selectedDay = ""
                                        }
                                    )
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(215.dp),
                                onDraw = {
                                    val barHeight = (value / maxValue) * size.height - 20
                                    drawRoundRect(
                                        color = if (day == currentDay) MainBlue else TranslucentBlue,
                                        topLeft = Offset(
                                            x = (size.width / 2) - 20,
                                            y = size.height - barHeight
                                        ),
                                        size = Size(40f, barHeight),
                                        cornerRadius = CornerRadius(10f, 10f)
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = day,
                                fontSize = 12.sp,
                                color = DarkBlue
                            )

                            if (day == currentDay || day == selectedDay) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .background(
                                            color = TranslucentBlue.copy(alpha = 0.6f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = value.toString(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkBlue.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

