package com.health.vita.meals.presentation

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.R
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.theme.VitaTheme

@Composable
fun HeartToggle() {
    var isFavorite by remember { mutableStateOf(false) }

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
            .clickable { isFavorite = !isFavorite }
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
        MealDetailScreen()
    }
}


@Composable
fun MealDetailScreen(navController: NavController = rememberNavController()) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
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
                                HeartToggle()

                            }
                            Image(
                                painter = painterResource(id = R.drawable.proteina),
                                contentDescription = "",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tacos Vegetales",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(text = "100g", style = MaterialTheme.typography.titleSmall)
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
                                text = "457cal",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.graphicsLayer(alpha = 0.5f)
                            )
                        }
                    }
                    Column {
                        MacronutrientDetails(
                            grams = 100f,
                            totalGrams = 1000f,
                            macronutrientType = MacronutrientType.FAT
                        )
                    }
                    Column {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text(text = "Ingredientes", style = MaterialTheme.typography.titleSmall)
                        }
                        Ingredient()
                    }
                }

            }
        }
    )
}

@Composable
fun Ingredient() {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Tortilla", style = MaterialTheme.typography.bodyLarge)
            Text(text = "200g", style = MaterialTheme.typography.bodyLarge)
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
    macronutrientType: MacronutrientType
) {
    val percentage = if (totalGrams > 0) grams / totalGrams else 0f

    val color = when (macronutrientType) {
        MacronutrientType.PROTEIN -> Color(0xFF8DCD92)
        MacronutrientType.CARBOHYDRATE -> Color(0xFFF8D558)
        MacronutrientType.FAT -> Color(0xFFF47551)
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
    Row(modifier = Modifier.padding(vertical = 12.dp)){
        Column(modifier = Modifier.padding(horizontal =  12.dp)){
            Image(painter = painterResource(id = icon), contentDescription = "", modifier = Modifier.size(24.dp))
        }
        Column(verticalArrangement = Arrangement.Center){
            Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = macroName, style = MaterialTheme.typography.bodyMedium)
                Text(text = ""+grams.toInt()+"g", style = MaterialTheme.typography.labelSmall, modifier = Modifier.graphicsLayer(alpha = 0.5f))
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




