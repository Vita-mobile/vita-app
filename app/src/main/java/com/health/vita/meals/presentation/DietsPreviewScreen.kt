package com.health.vita.meals.presentation

import DietsPreviewViewModel
import android.graphics.drawable.shapes.Shape
import android.util.Log.e
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.draw.clip
import com.health.vita.meals.domain.model.Meal
import com.health.vita.meals.utils.MacronutrientType
import kotlinx.coroutines.launch
import java.util.Calendar


@Composable
fun DietsPreviewScreen(
    navController: NavController = rememberNavController(),
    meal: Int
) {
    val dietsPreviewViewModel: DietsPreviewViewModel = viewModel(
        factory = DietsPreviewViewModelFactory(LocalContext.current)
    )

    val scope = rememberCoroutineScope()

    var possibleRefetch = 3

    val uiState by dietsPreviewViewModel.uiState.observeAsState(UiState.Idle)

    var selectedOption by remember { mutableStateOf("Mi plan") }


    val mealsIA by dietsPreviewViewModel.mealsIA.observeAsState(emptyList())

    val favorites by dietsPreviewViewModel.favorites.observeAsState(emptyList())

    val creations by dietsPreviewViewModel.creations.observeAsState(emptyList())


    val meals = when (selectedOption) {
        "Favoritas" -> favorites
        "Mi plan" -> mealsIA
        "Creaciones" -> creations
        else -> mealsIA
    }

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
            Toast.makeText(context, "Hubo un error al consumir la comida", Toast.LENGTH_SHORT).show()
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
                        navController.popBackStack()
                    }
                ) {
                    Text("Volver")
                }
            }
        )
    }

    val dataFlow = getValueAndTimestamp(context, DataStoreKeys.IA_REFETCH).collectAsState(
        initial = Pair(
            0,
            0L
        )
    )
    val (storedValue, lastUpdated) = dataFlow.value

    LaunchedEffect(storedValue, lastUpdated) {
        val currentTime = System.currentTimeMillis()
        val midnightToday = Calendar.getInstance().apply {
            timeInMillis = currentTime
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        if (lastUpdated != 0L) {
            if (lastUpdated < midnightToday) {

                possibleRefetch = 3
                scope.launch(Dispatchers.IO) {
                    saveValueAndTimestamp(context, 3, currentTime, DataStoreKeys.IA_REFETCH)
                }
            } else {
                possibleRefetch = storedValue
            }
        }
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
        dietsPreviewViewModel.loadOrGenerateMealsIA(meal)
        dietsPreviewViewModel.loadFavorites()
        dietsPreviewViewModel.loadCreations()
    }
    LaunchedEffect(selectedOption) {
        if (meals.isNotEmpty()) {
            pagerState.scrollToPage(0)
        }
    }

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
                    listOf("Favoritas", "Mi plan", "Creaciones").forEach { option ->
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .selectable(
                                    selected = selectedOption == option,
                                    onClick = {
                                        selectedOption = option
                                        selectedMeal = when (option) {
                                            "Favoritas" -> favorites.firstOrNull()
                                            "Mi plan" -> mealsIA.firstOrNull()
                                            else -> creations.firstOrNull()
                                        }
                                    }
                                )
                                .padding(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                            if(uiState == (UiState.Loading)){
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    CircularProgressIndicator()
                                }
                            }else {

                            Text(
                                text = "No se encontraron comidas",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            }

                        } else {
                            val currentPage = pagerState.currentPage.coerceIn(0, meals.size - 1)
                            selectedMeal = meals.getOrNull(currentPage)

                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                pageSize = PageSize.Fill,
                                pageSpacing = 8.dp,
                                userScrollEnabled = true,
                                verticalAlignment = Alignment.CenterVertically,
                                snapPosition = SnapPosition.Start
                            ) { pageIndex ->
                                val meal_ = meals[pageIndex]
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                    ,
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {



                                    Text(text = meal_.name, modifier = Modifier.padding(vertical = 10.dp))
                                    val mealJson = Gson().toJson(meal_)
                                    val isFavorite = favorites.contains(meal_)

                                    Box (

                                        Modifier.weight(1f)

                                    ){

                                        if (uiState is UiState.Loading) {
                                            CircularProgressIndicator()
                                        } else{

                                            MealCardComponent(meal_.ingredientMeals.sumOf { it.grams.toInt() }.toFloat(),meal_.proteins,meal_.carbs,meal_.fats, navController, mealJson, isFavorite)

                                        }


                                    }



                                    Row(

                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 32.dp)
                                            .fillMaxHeight(0.12f)
                                            ,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ){


                                        PrimaryIconButton(
                                            text = "Consumir",
                                            onClick = { showConfirmDialog = true },
                                            arrow = true,
                                            color = MaterialTheme.colorScheme.onTertiary,
                                            modifier = Modifier.padding(vertical = 10.dp)
                                        )


                                        Spacer(modifier = Modifier.width(4.dp))


                                        if (selectedOption == "Mi plan" && possibleRefetch > 0) {
                                            Button(

                                                onClick = {
                                                    dietsPreviewViewModel.rechargeMealsIA(meal)
                                                    possibleRefetch--
                                                    scope.launch(Dispatchers.IO) {
                                                        saveValueAndTimestamp(
                                                            context,
                                                            possibleRefetch,
                                                            System.currentTimeMillis(),
                                                            DataStoreKeys.IA_REFETCH
                                                        )
                                                    }
                                                },
                                                modifier = Modifier
                                                    .clip(CircleShape)
                                                    .wrapContentSize()
                                                    .border(1.dp, Color.LightGray, CircleShape)
                                                ,
                                                shape = CircleShape,
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.Transparent,
                                                    contentColor = MaterialTheme.colorScheme.primary
                                                )
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.outline_refresh_24),
                                                    contentDescription = "Refresh",
                                                    modifier = Modifier.wrapContentSize()
                                                )
                                            }
                                        }
                                    }


                                }
                            }


                        }
                        if (selectedOption == "Creaciones") {
                            Column(modifier = Modifier
                                .fillMaxSize()
                                .clickable { navController.navigate("CreateMeal") }, horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Bottom){
                                Image(painter = painterResource(id = R.drawable.baseline_add_24), contentDescription = "add meal", modifier = Modifier.size(56.dp))
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
                                dietsPreviewViewModel.loadOrGenerateMealsIA(meal)
                                dietsPreviewViewModel.loadFavorites()
                                dietsPreviewViewModel.loadCreations()
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
    )
}



@Preview(showBackground = true)
@Composable
fun MealCardComponentPrev() {
    VitaTheme {
        DietsPreviewScreen(meal = 1)
    }
}


@Composable
fun MealCardComponent(
    totalWeight: Float =300f,
    protein: Float =100f,
    carb: Float=100f,
    fat: Float=100f,
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
            .clickable { navController.navigate("MealDetail/$mealJson/$isFavorite") }
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Column(horizontalAlignment = Alignment.CenterHorizontally
        ){

        Row(
            modifier = Modifier
                .background(Color(0xFFbbf4df), shape = RoundedCornerShape(1000.dp))
                .padding(40.dp)
        ) {
            val dominantImage = when {
                protein >  carb && protein > fat -> com.health.vita.R.drawable.proteina
                carb > protein && carb > fat -> com.health.vita.R.drawable.carbohidrato
                else -> com.health.vita.R.drawable.grasas
            }
            Image(painter = painterResource(id = dominantImage), contentDescription = "")
        }
        Row(modifier = Modifier.padding(top = 30.dp)) {
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
