package com.health.vita.register.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.health.vita.core.navigation.Screen.SEX_SELECTION
import com.health.vita.core.utils.DatabaseNames
import com.health.vita.register.presentation.viewmodel.SignupViewModel
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.theme.VitaTheme

@Composable
fun FitnessLevelSelectionScreen(
    navController: NavController = rememberNavController(),
    signupViewModel: SignupViewModel = viewModel()
) {
    var sliderPosition by remember { mutableFloatStateOf(signupViewModel.activityLevel.value?.toFloat() ?: 1f) }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->

            Column(
                Modifier.padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                GeneralTopBar(text = "Valoración", step = 4, total = 6, onClick = { navController.navigateUp() })

                Spacer(modifier = Modifier.height(35.dp))

                Column(Modifier.weight(1f)) {

                    Text(
                        text = "¿Cómo calificarías tu nivel de estado físico?",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(60.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        Thumb(icon = "?", h = 10, v = 5)

                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(horizontal = 10.dp)
                        ) {
                            Text(
                                text = "Arrastre para ajustar",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }


                    CircularFitnessSlider(
                        currentValue = sliderPosition.toInt(),
                        onValueChange = { newValue ->
                            sliderPosition = newValue.toFloat()
                            signupViewModel.setActivityLevel(newValue)
                        },
                        physicalLevel = DatabaseNames.physicalLevel,
                        primaryColor = MaterialTheme.colorScheme.primary

                        )

                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 34.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PrimaryIconButton(text = "Continuar", onClick = { navController.navigate(SEX_SELECTION) })
                }


            }

        })

}

@Composable
fun Thumb(icon: String, h: Int, v: Int) {
    Box(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = h.dp, vertical = v.dp)
    ) {
        Text(text = icon, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun CircularFitnessSlider(
    currentValue: Int,
    onValueChange: (Int) -> Unit,
    physicalLevel: Map<Int, String>,
    primaryColor: Color,

) {

    var sliderValue by remember { mutableIntStateOf(currentValue) }
    var totalDrag by remember { mutableFloatStateOf(0f) } //Total drag accumulator

    /*
            Modifier to handle drag gestures
            Handling of drag gestures.
          * specifically detects dragging gestures.
          * "pointerInput": allows is used to detect the user's gestures.
          *  “onDrag” is executed when the user drags the finger over the slider.
          * "change":represents the change in the gesture.
          *  "dragAmount":amount of movement in the x- and y-axis during the dragging process
          *  "change.consume()":prevents the change from spreading to other components, as it has been consumed*/

    val dragModifier = Modifier.pointerInput(Unit) {

        detectDragGestures(
            onDrag = { change, dragAmount ->
                change.consume()

                // Sensitivity adjustment(touch)
                val dragSpeedFactor = 0.0085f

                /*calculates the drag taking into account the horizontal movement of the finger
                 multiplied by the drag speed factor.*/

                totalDrag += dragAmount.x * dragSpeedFactor

                //Calculation of the new slider value based on the cumulated total
                val newSliderValue = (sliderValue + totalDrag.toInt()).coerceIn(0, 5)

                if (newSliderValue != sliderValue) {
                    sliderValue = newSliderValue
                    totalDrag = 0f //resetting the accumulator when the value changes
                    onValueChange(sliderValue)
                }
            }
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = dragModifier
            .padding(16.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {

            val path = Path().apply {
                moveTo(0f, size.height)
                quadraticTo(
                    size.width * 0.2f, size.height * 0.1f,
                    size.width, 0f
                )
            }

            drawPath(
                path = path,
                color = primaryColor,
                style = Stroke(width = 30f)
            )

            /*Generation of lines aligned with the curve: the direction of the curve at a
            specific point is calculated and then use that direction to find a line perpendicular to
            the curve.*/

            val numberOfLines = 6
            val controlX = size.width * 0.2f
            val controlY = size.height * 0.1f

            for (i in 0 until numberOfLines) {
                val t = i / (numberOfLines - 1).toFloat()

                // Calculation of the x, y position using the Bézier equation

                val x = (1 - t) * (1 - t) * 0f + 2 * (1 - t) * t * controlX + t * t * size.width
                val y = (1 - t) * (1 - t) * size.height + 2 * (1 - t) * t * controlY + t * t * 0f

                //Calculate the tangent

                //derivative of the Bézier curve at a point determined by the parameter t
                val dx = 2 * (1 - t) * (controlX - 0f) + 2 * t * (size.width - controlX) //rate of change of the horizontal position (x-axis) along the curve
                val dy = 2 * (1 - t) * (controlY - size.height) + 2 * t * (0f - controlY) //rate of change of the vertical position (y-axis) along the curve

                //Standardization

                //vector magnitude
                val norm = kotlin.math.sqrt(dx * dx + dy * dy)
                val length = 100f

                //Calculation of the normal: vector perpendicular to vector (dx,dy)

                val px = -dy / norm * length
                val py = dx / norm * length

                drawLine(
                    color = Color.Black,
                    start = Offset(x - px / 2, y - py / 2),
                    end = Offset(x + px / 2, y + py / 2),
                    strokeWidth = 4f
                )
            }

            /*calculation of the position of the button in the slider, using the quadratic Bézier equation
             to determine the location of the button as a function of the current value of the slider
             (sliderValue)*/

            //Adjustment of `t` to align it with the slider value, according to the bezier equation, t goes from 0 to 1
            val t = sliderValue / 5f

            //calculation of the x and y position: the position of the button on the Bézier curve

            val x = (1 - t) * (1 - t) * 0f + 2 * (1 - t) * t * controlX + t * t * size.width
            val y = (1 - t) * (1 - t) * size.height + 2 * (1 - t) * t * controlY + t * t * 0f

            //button design
            drawRoundRect(
                color = Color(0xFF269be0),
                topLeft = Offset(x - 60f, y - 60f),
                size = androidx.compose.ui.geometry.Size(120f, 120f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f, 20f)
            )


        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 16.dp, top = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = sliderValue.toString(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 180.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                )
                Text(
                    text = physicalLevel[sliderValue] ?: "Desconocido",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview2() {
    VitaTheme {
        FitnessLevelSelectionScreen(signupViewModel = viewModel())
    }
}

