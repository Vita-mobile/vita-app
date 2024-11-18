package com.health.vita.meals.presentation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.R
import com.health.vita.meals.data.datastore.DataStoreKeys
import com.health.vita.meals.data.datastore.getValueAndTimestamp
import com.health.vita.meals.data.datastore.saveValueAndTimestamp
import com.health.vita.profile.presentation.viewModel.ProfileViewModel
import com.health.vita.ui.components.general.CustomPopup
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.meals.WaterIntakeTracker
import com.health.vita.ui.theme.Cyan
import com.health.vita.ui.theme.MintGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar


@Preview
@Composable
fun HydrationScreen(
    navController: NavController = rememberNavController(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val userState by profileViewModel.user.observeAsState()

    val weight = userState?.weight ?: 0f

    Log.e(">>>", "El peso es $weight")

    LaunchedEffect(Unit) {
        profileViewModel.getCurrentUser()
    }

    val waterIntakeGoal = if (weight == 0f) 2000 else (35 * weight).toInt()

    var waterIntake by remember {
        mutableStateOf(0)
    }

    var openWaterPopup by remember {
        mutableStateOf(false)
    }

    var waterAddition by remember {
        mutableStateOf(500)
    }

    val dataFlow = getValueAndTimestamp(context, DataStoreKeys.HYDRATION).collectAsState(initial = Pair(0, 0L))
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

        if (lastUpdated != 0L){
            if (lastUpdated < midnightToday) {

                waterIntake = 0
                scope.launch(Dispatchers.IO) {
                    saveValueAndTimestamp(context, 0, currentTime, DataStoreKeys.HYDRATION)
                }
            } else {
                waterIntake = storedValue
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), content = { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GeneralTopBar(text = "Hidratación", hasStep = false, onClick = { navController.navigateUp() })
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "${waterIntakeGoal}", style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 50.sp
                    )

                )
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = "ml",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
            Text(
                modifier = Modifier.padding(
                    top = 12.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                text = if (waterIntake < waterIntakeGoal) "Aun te falta consumir ${waterIntakeGoal - waterIntake} ml" else "Has llegado a tu meta de hidratación, ve!",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
                color = Color(0xFF414141),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter
            ) {
                WaterIntakeTracker(
                    totalWaterMl = waterIntakeGoal,
                    currentWaterMl = waterIntake
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${waterIntakeGoal}ml",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier

                    )
                    Text(
                        text = "Meta",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
                        modifier = Modifier
                    )
                }
                IconButton(
                    onClick = { openWaterPopup = !openWaterPopup },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(32.dp)
                        .clip(shape = CircleShape)
                        .background(color = Cyan)
                        .size(56.dp),
                ) {
                    Icon(
                        Icons.Filled.Add,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = stringResource(id = R.string.shopping_cart_content_desc)
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${waterIntake}ml",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 26.sp),
                        modifier = Modifier

                    )
                    Text(
                        text = "Actual",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                    )
                }

            }

            CustomPopup(
                showDialog = openWaterPopup,
                onDismiss = { },
                title = "¿Cuanta agua tomaste?",
                height = 0.3f,
                onCancel = {
                    openWaterPopup = false
                    waterAddition = 500
                },
                onConfirm = {
                    waterIntake += waterAddition
                    scope.launch(Dispatchers.IO) {
                        saveValueAndTimestamp(context, waterIntake, System.currentTimeMillis(), DataStoreKeys.HYDRATION)
                    }
                    openWaterPopup = !openWaterPopup
                    waterAddition = 500
                },
                content = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(start = 40.dp, end = 40.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(
                                        RoundedCornerShape(
                                            bottomStart = 28.dp,
                                            topStart = 28.dp
                                        )
                                    )
                            ) {
                                TextField(
                                    modifier = Modifier,
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = MintGreen,
                                        unfocusedContainerColor = MintGreen,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                                    value = "$waterAddition",
                                    onValueChange = { waterToAdd ->
                                        // Verifica si el input es numérico
                                        val number = waterToAdd.toIntOrNull()
                                        if (number != null) {
                                            waterAddition = number
                                        } else {
                                            if (waterToAdd == "") waterAddition = 0
                                            Toast.makeText(
                                                context,
                                                "Ingrese un valor válido",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Activa teclado numérico
                                )
                            }
                            Spacer(modifier = Modifier.width(1.dp))
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp))
                                    .background(MintGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "ml",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            )
        }

    }
    )

}
