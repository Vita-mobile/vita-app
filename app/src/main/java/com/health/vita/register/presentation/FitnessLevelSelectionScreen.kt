    package com.health.vita.register.presentation

    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.fillMaxHeight
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Slider
    import androidx.compose.material3.SliderDefaults
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavController
    import androidx.navigation.compose.rememberNavController
    import com.health.vita.core.navigation.Screen.HEIGHT_SELECTION
    import com.health.vita.core.navigation.Screen.SEX_SELECTION
    import com.health.vita.register.presentation.viewmodel.SignupViewModel
    import com.health.vita.ui.components.general.GeneralTopBar
    import com.health.vita.ui.components.general.PrimaryIconButton


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FitnessLevelSelectionScreen(navController: NavController = rememberNavController(), signupViewModel: SignupViewModel) {
        var sliderPosition by remember { mutableStateOf(signupViewModel.activityLevel.value?.toFloat() ?: 1f)}
        val textForValue = mapOf(
            1 to "Sedentario",
            2 to "Ligero",
            3 to "Moderado",
            4 to "Atlético",
            5 to "Muy activo"
        )
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceAround) {
            GeneralTopBar(onClick = { navController.navigate(HEIGHT_SELECTION) },text="Valoración", step = 4, total = 6)
            Column(verticalArrangement = Arrangement.SpaceAround){
            Text(text = "¿Como calificarias tu nivel de estado físico?", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleLarge)
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)){
                thumb(icon = "?", 10, 5)
                Column(modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 10.dp)) {
                    Text(text = "Arrastre para ajustar")
                }
            }
            Slider(
                value = sliderPosition,
                onValueChange = { newValue ->
                    sliderPosition = if (newValue < 1f) 1f else newValue;
                    signupViewModel.setActivityLevel(newValue.toInt());
                },                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTickColor = Color.Black
                ),
                steps = 4,
                valueRange = 0f..5f,
                modifier = Modifier.padding(horizontal = 24.dp),
                thumb = { thumb("( )", 12, 10)}
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = sliderPosition.toInt().toString(),
                        fontSize = 120.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = textForValue[sliderPosition.toInt()] ?: "Desconocido", // Usa el valor del Map o muestra "Desconocido" si no existe
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
            }
                Column(Modifier.fillMaxHeight().padding(vertical = 34.dp), verticalArrangement = Arrangement.Bottom){
                    PrimaryIconButton(text ="Continuar", onClick = { navController.navigate(SEX_SELECTION) })
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun preview(){ 
        FitnessLevelSelectionScreen(signupViewModel = viewModel())
    }

    //@Preview(showBackground = true)
    @Composable
    fun prev2(){
        thumb("( )", 15, 10);
    }

    @Composable
    fun thumb(icon: String, h: Int, v: Int){
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                ) // Esquinas redondeadas
                .padding(horizontal = h.dp, vertical = v.dp)
        ) {
            Text(text = icon, color = MaterialTheme.colorScheme.onPrimary);
        }
    }