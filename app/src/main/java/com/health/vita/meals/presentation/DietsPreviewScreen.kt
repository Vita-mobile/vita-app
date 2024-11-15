package com.health.vita.meals.presentation

import DietsPreviewViewModel
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.health.vita.R
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.meals.presentation.viewModels.DietsPreviewViewModelFactory
import com.health.vita.ui.components.general.PrimaryIconButton

@Composable
fun DietsPreviewScreen(
    navController: NavController = rememberNavController(),
    meal: Int
) {
    val dietsPreviewViewModel: DietsPreviewViewModel = viewModel(
        factory = DietsPreviewViewModelFactory(LocalContext.current)
    )

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

    val consumeMealState by dietsPreviewViewModel.consumeMealState.observeAsState(false)

    val context = LocalContext.current

    var hasConsumed by remember { mutableStateOf(false) }

    LaunchedEffect(consumeMealState) {
        if (consumeMealState && !hasConsumed) {
            hasConsumed = true
            navController.popBackStack()
        } else if (consumeMealState && hasConsumed) {
            Toast.makeText(context, "Hubo un error al consumir la comida", Toast.LENGTH_SHORT).show()
        }
    }

    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmación") },
            text = { Text("¿Deseas consumir esta comida?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedMeal?.let {
                            dietsPreviewViewModel.consumeMeal(it)
                        }
                        showConfirmDialog = false
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    LaunchedEffect(true) {
        dietsPreviewViewModel.loadOrGenerateMealsIA(meal)
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
                            selectedMeal = meals[pagerState.currentPage]

                            // HorizontalPager
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(500.dp)
                                    .padding(horizontal = 16.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                pageSize = PageSize.Fill,
                                pageSpacing = 8.dp,
                                userScrollEnabled = true,
                                verticalAlignment = Alignment.CenterVertically,
                                snapPosition = SnapPosition.Start
                            ) { pageIndex ->
                                val meal_ = meals[pageIndex]
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    if (selectedOption == "Mi plan") {
                                        Button(
                                            onClick = { dietsPreviewViewModel.rechargeMealsIA(meal) },
                                            modifier = Modifier
                                                .size(72.dp)
                                                .align(Alignment.CenterHorizontally)
                                                .border(1.dp, Color.LightGray, CircleShape),
                                            shape = CircleShape,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color.Transparent,
                                                contentColor = MaterialTheme.colorScheme.primary
                                            )
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.outline_refresh_24),
                                                contentDescription = "Refresh",
                                                modifier = Modifier.size(54.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(32.dp))
                                    Text(
                                        text = meal_.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedMeal = meal_
                                            }
                                            .wrapContentWidth(Alignment.CenterHorizontally)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Ver detalles",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .clickable {
                                                val mealJson = Gson().toJson(meal_)
                                                val isFavorite = favorites.contains(meal_)
                                                navController.navigate("MealDetail/$mealJson/$isFavorite")
                                            }
                                    )


                                    Spacer(modifier = Modifier.height(32.dp))

                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            "Proteínas: ${meal_.proteins}g",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Text(
                                            "Grasas: ${meal_.fats}g",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Text(
                                            "Carbohidratos: ${meal_.carbs}g",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(32.dp))
                                    PrimaryIconButton(
                                        text = "Consumir",
                                        onClick = {showConfirmDialog = true},
                                        arrow = true,
                                        )
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