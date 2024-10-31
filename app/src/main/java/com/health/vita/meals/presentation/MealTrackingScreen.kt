package com.health.vita.meals.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.core.utils.DatesFormat
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.theme.Dimens
import java.time.LocalDate
import java.util.Locale

// Clase de datos para MealSummary
data class MealSummaryData(
    val mealName: String,
    val calories: Float,
    val carbs: Float,
    val protein: Float,
    val fats: Float,
    val totalGrams: Float
)

@Composable
fun MealTrackingScreen(navController: NavController = rememberNavController()) {
    var selectedDateIndex by remember { mutableStateOf(0) }
    val listState = rememberLazyListState()

    val dates = remember {
        List(30) { index -> LocalDate.now().plusDays(index.toLong()) }
    }

    // Lista de comidas
    val meals = listOf(
        MealSummaryData(
            mealName = "Tamal valluno",
            calories = 300f,
            carbs = 45f,
            protein = 10f,
            fats = 5f,
            totalGrams = 60f
        ),
        MealSummaryData(
            mealName = "Pollo picao",
            calories = 350f,
            carbs = 20f,
            protein = 25f,
            fats = 5f,
            totalGrams = 50f
        ),
        MealSummaryData(
            mealName = "Maduro con queso",
            calories = 400f,
            carbs = 15f,
            protein = 15f,
            fats = 30f,
            totalGrams = 60f
        )
    )

    LaunchedEffect(selectedDateIndex) {
        listState.animateScrollToItem(selectedDateIndex)
    }

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                /*----------------Meals tracking header---------------------*/
                GeneralTopBar(
                    text = "Seguimiento",
                    onClick = { navController.popBackStack() },
                    hasStep = false,
                )
                Spacer(modifier = Modifier.height(24.dp))

                /*----------------Date selector---------------------*/
                LazyRow(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.13f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(dates) { index, date ->
                        DateCard(
                            isSelected = index == selectedDateIndex,
                            date = date,
                            onClick = {
                                selectedDateIndex = index
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                /*----------------Meals title---------------------*/
                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxHeight(0.09f)
                        .clip(shape = RoundedCornerShape(Dimens.borderRadius))
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Comidas",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                /*----------------Meals tracking list---------------------*/
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(meals) { index, meal ->
                        MealSummary(
                            mealName = meal.mealName,
                            calories = meal.calories,
                            carbs = meal.carbs,
                            protein = meal.protein,
                            fats = meal.fats,
                            totalGrams = meal.totalGrams
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun DateCard(isSelected: Boolean = false, date: LocalDate, onClick: () -> Unit) {
    val backgroundColorCard =
        if (isSelected) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surface

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(Dimens.borderRadius))
            .width(70.dp)
            .fillMaxHeight()
            .background(backgroundColorCard)
            .clickable { onClick() },
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val monthColor =
            if (isSelected) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black
        val dayColor =
            if (isSelected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.scrim
        val monthAbbreviation =
            DatesFormat.monthAbbreviations[date.monthValue]?.uppercase(Locale.ROOT) ?: ""

        Text(
            text = monthAbbreviation,
            style = MaterialTheme.typography.labelLarge,
            color = monthColor
        )

        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.labelLarge,
            color = dayColor
        )
    }
}

@Composable
fun MacronutrientDetail(
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

    val macroName = when (macronutrientType) {
        MacronutrientType.PROTEIN -> "Proteína"
        MacronutrientType.CARBOHYDRATE -> "Carbos"
        MacronutrientType.FAT -> "Grasas"
    }

    Row(
        modifier = Modifier
            .wrapContentWidth()
            .height(60.dp)
        , // Definir una altura fija para asegurar visibilidad
        verticalAlignment = Alignment.Bottom

        ,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .width(10.dp)

                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp))

                .background(MaterialTheme.colorScheme.surface)
            ,
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(percentage)
                    .fillMaxWidth()
                    .background(color)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "${grams}g",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = macroName,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun MealSummary(
    mealName: String,
    calories: Float,
    carbs: Float,
    protein: Float,
    fats: Float,
    totalGrams: Float,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // Ajusta la altura al contenido
            .border(
                1.dp,
                MaterialTheme.colorScheme.surfaceContainerHighest,
                RoundedCornerShape(Dimens.borderRadius)
            )
            .padding(16.dp)
    ) {
        /*--------------------------Meal title and details--------------------------*/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mealName,
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = com.health.vita.R.drawable.fuego),
                        contentDescription = "Calories",
                    )

                    Text(
                        text = "${calories} Kcal -",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.scrim
                    )

                    Text(
                        text = "${totalGrams} g",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.scrim
                    )
                }
            }

            val imageMealIndicator = when {
                protein > carbs && protein > fats -> com.health.vita.R.drawable.proteina
                carbs > protein && carbs > fats -> com.health.vita.R.drawable.carbohidrato
                else -> com.health.vita.R.drawable.grasas
            }

            Box(modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(id = imageMealIndicator),
                    contentDescription = "Favorite",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        /*--------------------------Macronutrients section--------------------------*/
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MacronutrientDetail(
                grams = protein,
                totalGrams = totalGrams,
                macronutrientType = MacronutrientType.PROTEIN
            )
            MacronutrientDetail(
                grams = carbs,
                totalGrams = totalGrams,
                macronutrientType = MacronutrientType.CARBOHYDRATE
            )
            MacronutrientDetail(
                grams = fats,
                totalGrams = totalGrams,
                macronutrientType = MacronutrientType.FAT
            )
        }
    }
}
