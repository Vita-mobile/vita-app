package com.health.vita.register.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.auth.presentation.viewmodel.SignupViewModel
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.theme.VitaTheme
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun AgeSelectionScreen(navController: NavController = rememberNavController(), signupViewModel: SignupViewModel) {
    var selectedAge by remember { mutableStateOf(19) } // Edad predeterminada
    val ageRange = (17..99).toList()

    // Estado de la lista para controlar el desplazamiento
    val listState = rememberLazyListState()

    // Esto asegura que el selector inicie con el número 19 en el centro
    LaunchedEffect(Unit) {
        listState.scrollToItem(selectedAge - 17) // Desplaza la lista para que el 19 esté centrado
    }

    // Observador para actualizar selectedAge basado en el elemento del centro
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                // Calcula el índice del elemento en el centro
                val centerIndex = index + 2 // 2 porque estamos mostrando 5 elementos (2 arriba, 2 abajo)

                // Asegúrate de que solo actualizas selectedAge si ha cambiado el índice
                if (centerIndex in ageRange.indices) {
                    val newSelectedAge = ageRange[centerIndex]
                    if (newSelectedAge != selectedAge) {
                        selectedAge = newSelectedAge // Actualiza selectedAge al elemento central solo si ha cambiado
                    }
                }
            }
    }

    // Cantidad de elementos visibles en el picker
    val visibleItemsCount = 5
    val centerIndex = visibleItemsCount / 2 // El índice central de los elementos visibles

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //GeneralTopBar(text = "Valoración", step = 1, total = 6, onClick = { navController.navigateUp() })

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¿Cuál es tu edad?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Picker de edad centrado
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp), // Altura del picker visible ajustada para 5 elementos
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemsIndexed(ageRange) { index, age ->
                            // Distancia desde el centro
                            val distanceFromCenter = abs(index - (listState.firstVisibleItemIndex + centerIndex))
                            val alpha = 1f - (distanceFromCenter * 0.2f) // Transparencia basada en la distancia
                            val color = if (age == selectedAge) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = alpha)

                            // Box para centrar los números
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp), // Altura de cada elemento
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$age",
                                    fontSize = if (age == selectedAge) 36.sp else 28.sp, // Tamaño de fuente aumentado
                                    color = color,
                                    modifier = Modifier.graphicsLayer(alpha = alpha),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                PrimaryIconButton(
                    text = "Continuar",
                    onClick = {
                        if (selectedAge in 17..99) {
                            navController.navigate("weightSelection") // Ir a la pantalla de selección de peso
                        }
                    },
                    arrow = true
                )
            }
        }
    )
}






@Preview(showBackground = true)
@Composable
fun AgeSelectionPreview() {
    VitaTheme {
        AgeSelectionScreen(signupViewModel = SignupViewModel())
    }
}