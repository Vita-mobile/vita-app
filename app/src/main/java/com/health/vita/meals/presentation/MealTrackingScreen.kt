package com.health.vita.meals.presentation

import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.health.vita.core.utils.DatesFormat
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.meals.presentation.viewModels.MealTrackingViewModel
import com.health.vita.meals.utils.MacronutrientType
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
fun MealTrackingScreen(
    navController: NavController = rememberNavController(),
    mealTrackingViewModel: MealTrackingViewModel = viewModel()
) {


    val uiState by mealTrackingViewModel.uiState.observeAsState(UiState.Idle)

    val mealsFetch by mealTrackingViewModel.mealsOfADate.observeAsState(emptyList())




    // Observe the days since the user registered the nutritional plan
    val daysSinceRegisterNutritionalPlan by mealTrackingViewModel.daysSinceRegisterNutritionalPlan.observeAsState(
        0
    )

    // Use to keep track of the selected date
    var selectedDateIndex by remember { mutableStateOf(0) }

    // Use to give the list of dates a scrollable behavior
    val listState = rememberLazyListState()

    //Create the list of dates to track based on the days since the user registered the nutritional plan until now
    var listDates by remember { mutableStateOf<List<LocalDate>>(emptyList()) }

    // Use to keep track of the loading state
    var isMealTrackingLoading by remember { mutableStateOf(true) }

    // Use to keep track of the rendering of the dates lazy row, when is finished we can scroll to the selected date

    var isRenderedDatesLazyRow by remember { mutableStateOf(false) }


    LaunchedEffect(true) {

        Log.d("Current user", "Current user: ${Firebase.auth.currentUser?.uid}")
        mealTrackingViewModel.getRegisterPlanDate()

    }

    LaunchedEffect(daysSinceRegisterNutritionalPlan) {

        Log.e("MealTrackingScreen", "Days since register nutritional plan: $daysSinceRegisterNutritionalPlan")
        if (daysSinceRegisterNutritionalPlan == 0) {
            return@LaunchedEffect
        }

        Log.d(
            "MealTrackingScreen",
            "Days since register nutritional plan: $daysSinceRegisterNutritionalPlan"
        )
        // If the user has registered the nutritional plan less than 30 days ago, show the dates since the registration
        listDates = if (daysSinceRegisterNutritionalPlan < 30) {

            List(daysSinceRegisterNutritionalPlan) { index ->
                LocalDate.now().minusDays(index.toLong())
            }.reversed()


        } else {
            List(30) { index -> LocalDate.now().minusDays(index.toLong()) }.reversed()
        }

        selectedDateIndex = listDates.size - 1

        isMealTrackingLoading = false

    }


    LaunchedEffect(selectedDateIndex, isRenderedDatesLazyRow) {

        listState.animateScrollToItem(selectedDateIndex)
    }

    LaunchedEffect(selectedDateIndex, listDates) {

        Log.e("MealTrackingScreen", "Selected date index on launched effect: $selectedDateIndex")

        if (listDates.isEmpty()) {
            return@LaunchedEffect
        }
    
        Log.e("MealTrackingScreen", "Selected date index on launched effect: ${listDates[selectedDateIndex]}")

        Log.e("MealTrackingScreen", "Selected date index on launched effect: ${listDates[selectedDateIndex]}")

        mealTrackingViewModel.getMealsOfADate(listDates[selectedDateIndex])

    }

    LaunchedEffect(listDates) {

        Log.d("MealTrackingScreen", "List of dates: $listDates")

    }

    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding )
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

                if (isMealTrackingLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.13f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {

                    LazyRow(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.13f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        itemsIndexed(listDates) { index, date ->
                            DateCard(
                                isSelected = index == selectedDateIndex,
                                date = date,
                                onClick = {

                                    Log.e("MealTrackingScreen", "Selected date index: $index")
                                    selectedDateIndex = index
                                }
                            )
                            isRenderedDatesLazyRow = true
                        }
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

                when (uiState) {
                    is UiState.Error -> {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Error : ${(uiState as UiState.Error).error.message}",
                                style = MaterialTheme.typography.titleSmall
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

                        if(mealsFetch.isEmpty()){

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),

                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = "No tienes alimentos registrados para este día.",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }


                        }else{

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {

                                itemsIndexed(mealsFetch) { index, meal ->
                                    MealSummary(
                                        mealName = meal.name,
                                        calories = meal.calories,
                                        carbs = meal.carbs,
                                        protein = meal.proteins,
                                        fats = meal.fats,
                                        totalGrams = meal.carbs + meal.proteins + meal.fats
                                    )
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
            if (isSelected) Color.White else Color.Black
        val dayColor =
            if (isSelected) Color.White else MaterialTheme.colorScheme.scrim
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
            .height(60.dp), // Definir una altura fija para asegurar visibilidad
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .width(10.dp)

                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp))

                .background(MaterialTheme.colorScheme.surface),
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
