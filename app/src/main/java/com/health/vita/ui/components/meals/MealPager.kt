package com.health.vita.ui.components.meals

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.health.vita.meals.domain.model.Meal
import androidx.compose.runtime.Composable

@Composable
fun MealPager(
    meal: Int,
    meals: List<Meal>,
    favorites: List<Meal>,
    pagerState: PagerState,
    dynamicHeightFraction: Float,
    navController: NavController
) {

    DietsOptions(meal = meal, navController = navController)

    Spacer(modifier = Modifier.height(16.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(dynamicHeightFraction)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 8.dp
        ) { pageIndex ->
            val meal_ = meals[pageIndex]
            val mealJson = Gson().toJson(meal_)
            val isFavorite = favorites.contains(meal_)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                MealCardComponent(
                    meal_.name,
                    meal_.ingredientMeals.sumOf { it.grams.toInt() }.toFloat(),
                    meal_.proteins,
                    meal_.carbs,
                    meal_.fats,
                    navController,
                    mealJson,
                    isFavorite
                )
            }
        }
    }
}