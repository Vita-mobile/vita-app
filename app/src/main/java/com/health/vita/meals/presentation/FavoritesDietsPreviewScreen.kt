package com.health.vita.meals.presentation

import FavoritesDietsPreviewViewModel
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.health.vita.R
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.meals.data.datastore.DataStoreKeys
import com.health.vita.meals.data.datastore.getValueAndTimestamp
import com.health.vita.meals.data.datastore.saveValueAndTimestamp
import com.health.vita.meals.presentation.viewModels.DietsPreviewViewModelFactory
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.theme.VitaTheme
import kotlinx.coroutines.Dispatchers
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import com.health.vita.meals.utils.MacronutrientType
import com.health.vita.ui.components.meals.MealCardComponent
import com.health.vita.ui.components.meals.MealPager
import kotlinx.coroutines.launch
import java.util.Calendar


@Composable
fun FavoritesDietsPreviewScreen(
    navController: NavController = rememberNavController(),
    meal: Int
) {
    val dietsPreviewViewModel: FavoritesDietsPreviewViewModel = viewModel(
        factory = DietsPreviewViewModelFactory(LocalContext.current)
    )
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var dynamicHeighFraction = 0f
    if(screenWidth<=427.dp){
        dynamicHeighFraction = 0.89f
    }else if(screenWidth<=1024.dp){
        dynamicHeighFraction = 0.80f
    }

    val uiState by dietsPreviewViewModel.uiState.observeAsState(UiState.Idle)

    val meals by dietsPreviewViewModel.favorites.observeAsState(emptyList())

    var selectedMeal by remember { mutableStateOf(meals.firstOrNull()) }

    val pagerState = rememberPagerState(
        pageCount = { meals.size }
    )

    val consumeMealState by dietsPreviewViewModel.consumeMealState.observeAsState(false)

    val context = LocalContext.current

    var hasConsumed by remember { mutableStateOf(false) }

    var showMealCreationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(consumeMealState) {
        if (consumeMealState && !hasConsumed) {
            hasConsumed = true
            showMealCreationDialog = true
        } else if (consumeMealState && hasConsumed) {
            Toast.makeText(context, "Hubo un error al consumir la comida", Toast.LENGTH_SHORT)
                .show()
        }
    }

    if (showMealCreationDialog) {
        AlertDialog(
            onDismissRequest = { showMealCreationDialog = false },
            title = { Text("Comida consumida") },
            text = { Text("¡Sigue así!.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showMealCreationDialog = false
                        navController.navigate("MealHome")
                    }
                ) {
                    Text("Volver")
                }
            }
        )
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
        dietsPreviewViewModel.loadFavorites()
    }


    Scaffold(
        content = { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                ) {
                    GeneralTopBar(
                        text = "Mis comidas",
                        onClick = { navController.navigate("MealHome") },
                        hasStep = false,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    when (uiState) {
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
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No hay comidas disponibles",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }else {
                                val currentPage = pagerState.currentPage.coerceIn(0, meals.size - 1)
                                selectedMeal = meals.getOrNull(currentPage)

                                MealPager(
                                    meal = meal,
                                    meals = meals,
                                    favorites = meals,
                                    pagerState = pagerState,
                                    dynamicHeightFraction = dynamicHeighFraction,
                                    navController = navController
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 32.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    PrimaryIconButton(
                                        text = "Consumir",
                                        onClick = { showConfirmDialog = true },
                                        arrow = true,
                                        color = MaterialTheme.colorScheme.onTertiary,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                }


                            }
                        }

                        is UiState.Error -> {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Error al cargar las comidas",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    dietsPreviewViewModel.loadFavorites()
                                },
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
        }
    )
}