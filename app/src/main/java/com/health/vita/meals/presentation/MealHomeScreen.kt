package com.health.vita.meals.presentation

import MealsViewModel
import android.graphics.drawable.Icon
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.health.vita.R
import com.health.vita.core.navigation.Screen.DIET_SELECTION
import com.health.vita.core.navigation.Screen.HYDRATION
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.meals.data.datastore.DataStoreKeys
import com.health.vita.meals.data.datastore.getValueAndTimestamp
import com.health.vita.meals.data.datastore.saveValueAndTimestamp
import com.health.vita.ui.components.meals.MealsCarousel
import com.health.vita.ui.theme.Aquamarine
import com.health.vita.ui.theme.Cyan
import com.health.vita.ui.theme.LightBlue
import com.health.vita.ui.theme.LightTurquoise
import com.health.vita.ui.theme.VitaTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun MealHomeScreen(navController: NavController) {

    val context = LocalContext.current


    val mealsViewModel = MealsViewModel(context)

    val scope = rememberCoroutineScope()


    val lastRecordedMeal by mealsViewModel.lastRecordedMeal.observeAsState(0)
    val mealCount by mealsViewModel.mealCounts.observeAsState()

    val isToday by mealsViewModel.lastEatenMeal.observeAsState()
    val uiState by mealsViewModel.uiState.observeAsState(UiState.Idle)

    val backStackEntry = navController.currentBackStackEntryAsState()

    LaunchedEffect(Unit) {
        mealsViewModel.getCurrentUser()
    }

    val userState by mealsViewModel.user.observeAsState()

    val weight = userState?.weight ?: 0f
    val waterIntakeGoal = if (weight == 0f) 2000 else (35 * weight).toInt()


    var waterIntake by remember { mutableStateOf(0) }
    var sliderPosition by remember { mutableStateOf(0f) }


    val dataFlow = getValueAndTimestamp(context, DataStoreKeys.HYDRATION).collectAsState(initial = Pair(0, 0L))
    val (storedValue, lastUpdated) = dataFlow.value




    LaunchedEffect(backStackEntry.value) {
        mealsViewModel.getCurrentMeal()
        mealsViewModel.getLastEatenMeal()
    }

    LaunchedEffect(true) {
        mealsViewModel.getCurrentMeal()
        mealsViewModel.getLastEatenMeal()
    }
    if (isToday == false) {
        mealsViewModel.resetMealIndex()
    }

    LaunchedEffect(storedValue, lastUpdated) {
        val currentTime = System.currentTimeMillis()
        val midnightToday = Calendar.getInstance().apply {
            timeInMillis = currentTime
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        if (lastUpdated < midnightToday) {

            waterIntake = 0
            scope.launch(Dispatchers.IO) {
                saveValueAndTimestamp(context, 0, currentTime, DataStoreKeys.HYDRATION)
            }
        } else {

            waterIntake = storedValue
        }

        sliderPosition = (waterIntake / waterIntakeGoal.toFloat()).coerceIn(0f, 1f)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Column {

                        Box(modifier = Modifier.weight(0.1f))

                        Text(
                            text = "Hidratación",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 24.sp
                            )
                        )

                        Box(modifier = Modifier.weight(0.05f))

                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .clickable {
                                    navController.navigate(HYDRATION)
                                }
                                .background(
                                    color = Color(0xFF2563EB),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 4.dp,
                                    color = Color(0xFF96B7FF),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .wrapContentSize(align = Alignment.BottomStart)

                        ) {
                            Row {

                                Icon(
                                    painter = painterResource(id = R.drawable.sharp_water_drop_24 ),
                                    contentDescription = "water icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "Consumo de agua",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.align( Alignment.CenterVertically)
                                )
                            }

                        }

                        Box(modifier = Modifier.weight(0.05f))

                        Column (modifier = Modifier.fillMaxWidth()
                            .height(130.dp)
                            .background(MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(20.dp),
                                )
                            .padding(horizontal = 18.dp),
                            verticalArrangement = Arrangement.SpaceAround){

                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(Color(0xFF4FC3F7), Color(0xFF81D4FA) , Color(0xFFA3E4ED))
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {

                                //Line
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val lineY = size.height / 2
                                    val lineEndX = sliderPosition * size.width
                                    drawLine(
                                        color = Color.White,
                                        start = Offset(0f, lineY),
                                        end = Offset(lineEndX, lineY),
                                        strokeWidth = 4.dp.toPx()
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .offset(x = sliderPosition  * (LocalDensity.current.density * 100).dp)
                                        .clip(RectangleShape)
                                        .background( Color(0xFF0087D1),
                                            RoundedCornerShape(14.dp)
                                        )
                                        .border(4.dp, Color.White, RoundedCornerShape(14.dp)),
                                    contentAlignment = Alignment.Center
                                ){
                                    Box(modifier = Modifier.size(19.dp)
                                        .background(color = LightBlue, RoundedCornerShape(4.dp)))
                                }
                            }


                            val water = buildAnnotatedString {
                                append("${(sliderPosition * waterIntakeGoal).toInt()} ")
                                withStyle(style = SpanStyle(color = Color.White.copy(alpha = 0.9f))) {
                                    append("ML")
                                }
                            }

                            Text(
                                text = water,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                            )


                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 18.dp)
                        ) {

                            Text(text = "Comidas", style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 24.sp
                            ))
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

                        Box(modifier = Modifier.weight(1f))

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