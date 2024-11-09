package com.health.vita.meals.presentation

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
import androidx.compose.ui.Modifier

data class Meal(val name: String, val protein: Int, val fats: Int, val carbs: Int)

val mealOption1 = listOf(
    Meal("Comida 1", protein = 25, fats = 10, carbs = 30),
    Meal("Comida 2", protein = 20, fats = 15, carbs = 40),
    Meal("Comida 3", protein = 30, fats = 5, carbs = 35)
)

val mealOption2 = listOf(
    Meal("Comida A", protein = 15, fats = 10, carbs = 45),
    Meal("Comida B", protein = 20, fats = 10, carbs = 50),
    Meal("Comida C", protein = 25, fats = 5, carbs = 55)
)

val mealOption3 = listOf(
    Meal("Comida X", protein = 35, fats = 5, carbs = 25),
    Meal("Comida Y", protein = 40, fats = 8, carbs = 30),
    Meal("Comida Z", protein = 30, fats = 10, carbs = 40)
)

@Composable
fun DietsPreviewScreen(navController: NavController = rememberNavController()) {
    var selectedOption by remember { mutableStateOf("Opción 1") }
    val meals = when (selectedOption) {
        "Opción 1" -> mealOption1
        "Opción 2" -> mealOption2
        else -> mealOption3
    }

    var selectedMeal by remember { mutableStateOf(meals.first()) }

    val pagerState = rememberPagerState(
        pageCount = { meals.size }
    )

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
                    listOf("Opción 1", "Opción 2", "Opción 3").forEach { option ->
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .selectable(
                                    selected = selectedOption == option,
                                    onClick = {
                                        selectedOption = option
                                        selectedMeal = when (option) {
                                            "Opción 1" -> mealOption1.first()
                                            "Opción 2" -> mealOption2.first()
                                            else -> mealOption3.first()
                                        }
                                    }
                                )
                                .padding(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                            Text("Proteínas: ${meal.protein}g", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(8.dp))

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Grasas: ${meal.fats}g", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(8.dp))

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Carbohidratos: ${meal.carbs}g", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                LaunchedEffect(pagerState.currentPage) {
                    selectedMeal = meals[pagerState.currentPage]
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
