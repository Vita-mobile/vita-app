package com.health.vita.register.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.health.vita.R
import com.health.vita.core.navigation.Screen.FITNESS_LEVEL_SELECTION
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.register.presentation.viewmodel.SignupViewModel
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.components.profile.Ruler
import com.health.vita.ui.theme.VitaTheme

@Composable
fun HeightSelectionScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    signupViewModel: SignupViewModel = viewModel()
) {
    val maxCm = 240

    val height by signupViewModel.height.observeAsState(170)
    var selectedValue by remember { mutableStateOf(height) }
    var quantity by remember { mutableStateOf(maxCm) }
    val uiState by signupViewModel.uiState.observeAsState(UiState.Idle)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->

            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primary),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)) {
                    //Modo Claro
                    GeneralTopBar(
                        text = "Valoración",
                        step = 2,
                        total = 6,
                        onClick = { navController.navigateUp() },
                        lightMode = false,
                    )
                    Box(modifier = Modifier.weight(1f))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp),
                        textAlign = TextAlign.Center,
                        text = "¿Cuál es tu altura actual?",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Box(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp),
                        textAlign = TextAlign.Center,
                        text = "$selectedValue Cm",
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.work_sans)
                            ), fontSize = 47.sp, fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Box(modifier = Modifier.weight(1f))
                    Ruler(quantity = quantity, onValueChange = { newValue ->
                        selectedValue = newValue
                        signupViewModel.setHeight(newValue.toFloat())
                    })

                    Box(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.padding(bottom = 36.dp)) {
                        PrimaryIconButton(
                            arrow = true,
                            onClick = {
                                navController.navigate(FITNESS_LEVEL_SELECTION)
                            },
                            blackContent = true,
                            text = "Continuar",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        })

}

@Preview(showBackground = true)
@Composable
fun HeightSelectionPreview() {
    VitaTheme {
        HeightSelectionScreen(signupViewModel = SignupViewModel())
    }
}
