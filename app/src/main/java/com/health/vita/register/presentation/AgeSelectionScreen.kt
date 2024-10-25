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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.core.navigation.Screen
import com.health.vita.register.presentation.viewmodel.SignupViewModel
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.theme.VitaTheme
import kotlin.math.abs

@Composable
fun AgeSelectionScreen(navController: NavController = rememberNavController(), signupViewModel: SignupViewModel = viewModel()) {

    var age by remember { mutableIntStateOf(18) }
    val ageRange = (11..99).toList()
    val listState = rememberLazyListState()


    LaunchedEffect(Unit) {
        listState.scrollToItem(age - 11)
    }


    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                val newSelectedAge = ageRange.getOrNull(index + 2)
                if (newSelectedAge != null && newSelectedAge != age) {
                    age = newSelectedAge
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
                // Barra superior
                GeneralTopBar(text = "Valoración", step = 1, total = 6, onClick = { navController.navigateUp() })
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "¿Cuál es tu edad?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(60.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp).padding(top = 40.dp, bottom = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemsIndexed(ageRange) { index, unselectedAge ->
                            val distanceFromCenter = abs(index - listState.firstVisibleItemIndex)
                            val alpha = 1f - (distanceFromCenter * 0.2f)
                            val scale = if (unselectedAge == age ) 1.2f else 0.85f

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(84.dp).padding(vertical = 15.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$unselectedAge",
                                    fontSize = 52.sp,
                                    color = Color.Gray.copy(alpha = alpha),
                                    modifier = Modifier.graphicsLayer(
                                        alpha = alpha,
                                        scaleX = scale,
                                        scaleY = scale
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }


                    Box(
                        modifier = Modifier
                            .size(110.dp)
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
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    PrimaryIconButton(
                        text = "Continuar",
                        onClick = {
                            if (age in 11..99) {
                                navController.navigate(Screen.WEIGHT_SELECTION)
                                signupViewModel.setAge(age)
                            }
                        },
                        arrow = true
                    )
                }
                Box(modifier = Modifier.weight(1f))
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
