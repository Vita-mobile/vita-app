package com.health.vita.meals.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.health.vita.meals.domain.model.Meal
import com.health.vita.ui.components.general.GeneralTopBar

@Composable
fun MealDetailScreen(
    navController: NavController = rememberNavController(),
    meal: String,
    isFavorite: Boolean
) {

    val gson = Gson()

    val mealObj: Meal = gson.fromJson(meal, Meal::class.java)

    Scaffold (
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ){

                GeneralTopBar(
                    text = "Seguimiento",
                    onClick = { navController.popBackStack() },
                    hasStep = false,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Name: ${mealObj.name}")
                Text(text = "Description: ${mealObj.description}")
                Text(text = "Calories: ${mealObj.calories}")
                Text(text = "Carbs: ${mealObj.carbs}")
                Text(text = "Fats: ${mealObj.fats}")
                Text(text = "Proteins: ${mealObj.proteins}")

                Text(text = "Ingredients:")
                mealObj.ingredients.forEach { ingredient ->
                    Text(text = "- ${ingredient.name}")
                }
            }
        }
    )
}

