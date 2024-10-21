package com.health.vita.register.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.core.navigation.Screen
import com.health.vita.register.presentation.viewmodel.SignupViewModel
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.theme.VitaTheme
import kotlin.math.abs

@Composable
fun AgeSelectionScreen(navController: NavController = rememberNavController(), signupViewModel: SignupViewModel) {

    var selectedAge by remember { mutableIntStateOf(18) } // default age

    //minimum age: 11
    val ageRange = (11..99).toList()
    val listState = rememberLazyListState()

    var firstVisibleItemIndex by remember { mutableIntStateOf(0) }


    LaunchedEffect(Unit) {
        listState.scrollToItem(selectedAge - 11)
    }


    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                firstVisibleItemIndex = index
                val newSelectedAge = ageRange.getOrNull(index + 2)
                if (newSelectedAge != null && newSelectedAge != selectedAge) {
                    selectedAge = newSelectedAge
                }
            }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GeneralTopBar(text = "Valoración", step = 1, total = 6, onClick = { navController.navigateUp() })
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "¿Cuál es tu edad?",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(130.dp))

                Box {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemsIndexed(ageRange) { index, age ->

                            // Use the visible index stored in the firstVisibleItemIndex
                            val distanceFromCenter = abs(index - firstVisibleItemIndex)

                            //design elements
                            val alpha = 1f - (distanceFromCenter * 0.15f) // Gradual reduction of opacity
                            val scale = if (age == selectedAge) 1.2f else 0.9f // Scale

                            if (age == selectedAge) {

                                // Selected age
                                Box(
                                    modifier = Modifier
                                        .graphicsLayer {
                                            scaleX = scale
                                            scaleY = scale
                                        }
                                        .size(100.dp)
                                        .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp))
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$age",
                                        fontSize = 58.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                // Unselected age
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(68.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$age",
                                        fontSize = 52.sp,
                                        color = Color.Gray.copy(alpha = alpha),
                                        modifier = Modifier.graphicsLayer(alpha = alpha, scaleX = scale, scaleY = scale),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    PrimaryIconButton(
                        text = "Continuar",
                        onClick = {
                            if (selectedAge in 11..99) {
                                navController.navigate(Screen.WEIGHT_SELECTION)
                            }
                        },
                        arrow = true
                    )
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun AgeSelectionPreview() {
    VitaTheme {
        AgeSelectionScreen(signupViewModel = SignupViewModel())
    }
}
