package com.health.vita.meals.presentation

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.R
import com.health.vita.meals.presentation.viewModels.MealDetailViewModel
import com.health.vita.meals.utils.MacronutrientType
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.theme.VitaTheme

@Composable
fun HeartToggle(
    fav: Boolean = false,
    onClick: (Boolean) -> Unit
) {
    var isFavorite by remember { mutableStateOf(fav) }
    val color by animateColorAsState(if (isFavorite) Color.Red else Color.Gray)
    val transition = updateTransition(targetState = isFavorite, label = "heartTransition")

    val scale by transition.animateFloat(
        transitionSpec = {
            keyframes {
                durationMillis = 400
                1.0f at 0 using LinearOutSlowInEasing
                1.5f at 150 using FastOutLinearInEasing
                1.0f at 300 using LinearOutSlowInEasing
            }
        },
        label = "scaleTransition"
    ) { state ->
        if (state) 1.5f else 1.0f
    }
    Box(
        modifier = Modifier
            .size(48.dp)
            .clickable {
                isFavorite = !isFavorite
                onClick(isFavorite)
            }
            .graphicsLayer(scaleX = scale, scaleY = scale, shape = CircleShape, clip = false),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = "Heart Toggle",
            tint = color
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MealDetailPrev() {
    VitaTheme {
        MealDetailScreen(
            meal = "{\n" +
                    "  \"id\": \"12345\",\n" +
                    "  \"name\": \"Grilled Chicken Salad\",\n" +
                    "  \"description\": \"A healthy salad with grilled chicken, fresh vegetables, and a light dressing.\",\n" +
                    "  \"calories\": 350.5,\n" +
                    "  \"carbs\": 20.0,\n" +
                    "  \"fats\": 10.5,\n" +
                    "  \"proteins\": 30.0,\n" +
                    "  \"ingredients\": [\n" +
                    "    {\n" +
                    "      \"id\": \"1\",\n" +
                    "      \"name\": \"Chicken Breast\",\n" +
                    "      \"quantity\": 200,\n" +
                    "      \"unit\": \"grams\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": \"2\",\n" +
                    "      \"name\": \"Lettuce\",\n" +
                    "      \"quantity\": 100,\n" +
                    "      \"unit\": \"grams\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": \"3\",\n" +
                    "      \"name\": \"Tomatoes\",\n" +
                    "      \"quantity\": 50,\n" +
                    "      \"unit\": \"grams\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"meal\": 1\n" +
                    "}\n", isFavorite = true
        )
    }
}


@Composable
fun MealDetailScreen(
    mealDetailViewModel: MealDetailViewModel = viewModel(),
    navController: NavController = rememberNavController(),
    meal: String,
    isFavorite: Boolean
) {
    val totalGrams by mealDetailViewModel.pesoTotal.observeAsState(0)
    val macroImage by mealDetailViewModel.macroDominantImage.observeAsState(com.health.vita.R.drawable.grasas)
    val mealObj by mealDetailViewModel.meal.observeAsState()

    LaunchedEffect(true) {
        mealDetailViewModel.setMealFromJson(meal)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.padding(bottom = 40.dp, start = 16.dp, end = 16.dp)) {
                    GeneralTopBar(
                        onClick = { navController.navigateUp() },
                        text = "Nutrición",
                        hasStep = false
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                HeartToggle(isFavorite) { isFav ->
                                    mealDetailViewModel.toggleFavorite(isFav)
                                }

                            }
                            Image(
                                painter = painterResource(id = macroImage),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }
                    }
                    Column(Modifier.verticalScroll(rememberScrollState())){


                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = mealObj?.name ?: "Meal",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "${totalGrams}g",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Valor nutricional",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.graphicsLayer(alpha = 0.5f)
                            )
                            Text(
                                text = "${mealObj?.calories?.toInt() ?: 0}cal",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.graphicsLayer(alpha = 0.5f)
                            )
                        }
                    }
                    Column {
                        MacronutrientDetails(
                            grams = mealObj?.proteins ?: 0f,
                            totalGrams = totalGrams.toFloat(),
                            macronutrientType = MacronutrientType.PROTEIN
                        )
                        MacronutrientDetails(
                            grams = mealObj?.carbs ?: 0f,
                            totalGrams = totalGrams.toFloat(),
                            macronutrientType = MacronutrientType.CARBOHYDRATE
                        )
                        MacronutrientDetails(
                            grams = mealObj?.fats ?: 0f,
                            totalGrams = totalGrams.toFloat() ?: 0f,
                            macronutrientType = MacronutrientType.FAT
                        )


                    }
                    Column {
                        if (!mealObj?.description.equals("")) {
                            Log.e(">>>", mealObj?.description?:"No hay")
                            Column(       modifier = Modifier
                                .fillMaxWidth()){
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Descripción",
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = mealObj?.description
                                            ?: "No hay descripción para esta comida"
                                    )
                                }

                            }
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Ingredientes", style = MaterialTheme.typography.titleSmall)
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            mealObj?.ingredientMeals?.forEach { ingredient ->
                                Ingredient(ingredient.name, ingredient.grams)
                            }
                        }
                    }
                    }
                }

            }
        }
    )
}

@Composable
fun Ingredient(
    name: String,
    weight: Float
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = name, style = MaterialTheme.typography.bodyLarge)
            Text(text = "${weight.toInt()}g", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onBackground)
        )
    }
}

@Composable
fun MacronutrientDetails(
    grams: Float,
    totalGrams: Float,
    macronutrientType: MacronutrientType,
    proteinColor: Long = 0xFF8DCD92,
    carbColor: Long = 0xFFF8D558,
    fatColor: Long = 0xFFF47551
) {
    val percentage = if (totalGrams > 0) grams / totalGrams else 0f

    val color = when (macronutrientType) {
        MacronutrientType.PROTEIN -> Color(proteinColor)
        MacronutrientType.CARBOHYDRATE -> Color(carbColor)
        MacronutrientType.FAT -> Color(fatColor)
    }

    val icon = when (macronutrientType) {
        MacronutrientType.PROTEIN -> R.drawable.proteina
        MacronutrientType.CARBOHYDRATE -> R.drawable.carbohidrato
        MacronutrientType.FAT -> R.drawable.grasas
    }

    val macroName = when (macronutrientType) {
        MacronutrientType.PROTEIN -> "Proteína"
        MacronutrientType.CARBOHYDRATE -> "Carbos"
        MacronutrientType.FAT -> "Grasas"
    }
    Row(modifier = Modifier.padding(vertical = 12.dp)) {
        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
        }
        Column(verticalArrangement = Arrangement.Center) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = macroName, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "" + grams.toInt() + "g",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.graphicsLayer(alpha = 0.5f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface),
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .height(5.dp)
                        .fillMaxWidth(percentage)
                        .background(color)
                )
            }
        }
    }
}
