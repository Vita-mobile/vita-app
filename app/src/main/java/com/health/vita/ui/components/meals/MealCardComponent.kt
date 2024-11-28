package com.health.vita.ui.components.meals

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.health.vita.meals.presentation.MacronutrientDetails
import com.health.vita.meals.utils.MacronutrientType

@Composable
fun MealCardComponent(
    name: String = "",
    totalWeight: Float = 300f,
    protein: Float = 100f,
    carb: Float = 100f,
    fat: Float = 100f,
    navController: NavController,
    mealJson: String,
    isFavorite: Boolean

) {
    Column(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Color(0xFF75ECC0),
                shape = RoundedCornerShape(1000.dp)
            )
            .fillMaxWidth()
            .background(Color(0xFFe9fbf5), shape = RoundedCornerShape(1000.dp))
            .padding(start = 30.dp, end = 30.dp, top = 20.dp, bottom = 100.dp)
            .clickable { navController.navigate("MealDetail/$mealJson/$isFavorite") },
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .background(Color(0xFFbbf4df), shape = RoundedCornerShape(1000.dp))
                    .padding(40.dp)
            ) {
                val dominantImage = when {
                    protein > carb && protein > fat -> com.health.vita.R.drawable.proteina
                    carb > protein && carb > fat -> com.health.vita.R.drawable.carbohidrato
                    else -> com.health.vita.R.drawable.grasas
                }
                Image(painter = painterResource(id = dominantImage), contentDescription = "")
            }

            Text(
                text = name,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            Row(modifier = Modifier.padding(top = 10.dp)) {
                MacronutrientDetails(
                    grams = protein,
                    totalGrams = totalWeight,
                    macronutrientType = MacronutrientType.PROTEIN,
                    proteinColor = 0xFFb60100
                )
            }
            Row {
                MacronutrientDetails(
                    grams = carb,
                    totalGrams = totalWeight,
                    macronutrientType = MacronutrientType.CARBOHYDRATE,
                    carbColor = 0xFF269ae1

                )
            }

            Row {
                MacronutrientDetails(
                    grams = fat,
                    totalGrams = totalWeight,
                    macronutrientType = MacronutrientType.FAT,
                    fatColor = 0xFFf9d458
                )
            }

        }


    }
}
