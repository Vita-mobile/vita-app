package com.health.vita.meals.presentation

import DietsPreviewViewModel
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.theme.VitaTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.health.vita.core.utils.states_management.UiState

@Composable
fun DietsPreviewScreen(
    navController: NavController = rememberNavController(),
    dietsPreviewViewModel: DietsPreviewViewModel = viewModel()
) {
    val uiState by dietsPreviewViewModel.uiState.observeAsState(UiState.Idle)

    var selectedOption by remember { mutableStateOf("Mi plan") }

    val mealsIA by dietsPreviewViewModel.mealsIA.observeAsState(emptyList())

    val favorites by dietsPreviewViewModel.favorites.observeAsState(emptyList())

    val meals = when (selectedOption) {
        "Favoritas" -> favorites
        "Mi plan" -> mealsIA
        "Crear" -> mealsIA
        else -> mealsIA
    }

    var selectedMeal by remember { mutableStateOf(meals.firstOrNull()) }

    val pagerState = rememberPagerState(
        pageCount = { meals.size }
    )

    LaunchedEffect(true) {
        dietsPreviewViewModel.loadOrGenerateMealsIA()
        dietsPreviewViewModel.loadFavorites()
    }

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                GeneralTopBar(
                    text = "Mis comidas",
                    onClick = { navController.popBackStack() },
                    hasStep = false,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("Favoritas", "Mi plan", "Crear").forEach { option ->
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .selectable(
                                    selected = selectedOption == option,
                                    onClick = {
                                        selectedOption = option
                                        selectedMeal = when (option) {
                                            "Favoritas" -> favorites.firstOrNull()
                                            "Mi plan" -> mealsIA.firstOrNull()
                                            else -> mealsIA.firstOrNull()
                                        }
                                    }
                                )
                                .padding(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (uiState){
                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is UiState.Success -> {
                        if (meals.isEmpty()) {
                            Text(
                                text = "No se encontraron comidas",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        } else {
                            // HorizontalPager
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                pageSize = PageSize.Fill,
                                pageSpacing = 8.dp,
                                userScrollEnabled = true,
                                verticalAlignment = Alignment.CenterVertically,
                                snapPosition = SnapPosition.Start
                            ) { pageIndex ->
                                val meal = meals[pageIndex]
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        text = meal.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedMeal = meal
                                            }
                                            .wrapContentWidth(Alignment.CenterHorizontally)
                                    )

                                    Spacer(modifier = Modifier.height(32.dp))

                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            "Proteínas: ${meal.proteins}g",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Text(
                                            "Grasas: ${meal.fats}g",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Text(
                                            "Carbohidratos: ${meal.carbs}g",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }
                        }
                    }
                    is UiState.Error -> {
                        Text(
                            text = "Error al cargar las comidas",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    is UiState.Idle -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }


                LaunchedEffect(pagerState.currentPage) {
                    if (meals.isNotEmpty()) {
                        selectedMeal = meals[pagerState.currentPage]
                    }
                }
            }
        }
    )
}





@Preview
@Composable
fun DietsPreview() {
    VitaTheme {
        DietsPreviewScreen()
    }
}
