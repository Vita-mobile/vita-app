package com.health.vita.meals.presentation

import MealsViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.health.vita.core.navigation.Screen.DIET_SELECTION
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.ui.components.meals.MealsCarousel
import com.health.vita.ui.theme.VitaTheme

@Composable
fun MealHomeScreen(navController: NavController) {
    val context = LocalContext.current
    val mealsViewModel = MealsViewModel(context)
    val lastRecordedMeal by mealsViewModel.lastRecordedMeal.observeAsState(0)
    val mealCount by mealsViewModel.mealCounts.observeAsState()
    val uiState by mealsViewModel.uiState.observeAsState(UiState.Idle)

    val backStackEntry = navController.currentBackStackEntryAsState()

    LaunchedEffect(backStackEntry.value) {
        mealsViewModel.getLastEatenMeal()
        mealsViewModel.getCurrentMeal()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 18.dp)
                        ) {
                            Text(text = "Comidas", style = MaterialTheme.typography.titleMedium)
                            Text(text = "Detalles",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .alpha(0.8f)
                                    .clickable { navController.navigate(DIET_SELECTION) })
                        }
                        when (uiState) {
                            is UiState.Error -> MealsCarousel(
                                mealCount ?: 0,
                                navController,
                                lastRecordedMeal
                            )

                            UiState.Idle -> MealsCarousel(
                                mealCount ?: 0,
                                navController,
                                lastRecordedMeal
                            )

                            UiState.Loading -> Box(
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }

                            UiState.Success -> MealsCarousel(
                                mealCount ?: 0,
                                navController,
                                lastRecordedMeal
                            )
                        }

                    }
                }
            }
        }
    )
}


@Composable
@Preview(showBackground = true)
fun MealHomePrev() {
    VitaTheme {
        MealHomeScreen(rememberNavController())
    }
}