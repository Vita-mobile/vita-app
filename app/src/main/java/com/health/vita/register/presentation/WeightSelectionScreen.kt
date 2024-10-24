package com.health.vita.register.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.register.presentation.viewmodel.SignupViewModel
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.health.vita.core.navigation.Screen.HEIGHT_SELECTION
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.components.profile.Ruler
import com.health.vita.ui.theme.VitaTheme

@Composable
    fun WeightSelectionScreen(modifier: Modifier = Modifier, navController: NavController = rememberNavController(), signupViewModel: SignupViewModel = viewModel()) {
    val maxKgs = 200
    val maxLbs = 400
    var selectedValue by remember { mutableStateOf(50) }
    var selectedUnit by remember { mutableStateOf("Kg") }
    var quantity by remember { mutableStateOf(maxKgs) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->

            Column(
                modifier = modifier.padding(innerPadding)
                    .fillMaxSize().padding(horizontal = 0.dp)
                    .background(color = MaterialTheme.colorScheme.primary),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Modo Claro
                GeneralTopBar(
                    text = "Valoración",
                    step = 2,
                    total = 6,
                    onClick = { navController.navigateUp() },
                    lightMode = false
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp),
                    textAlign = TextAlign.Center,
                    text = "¿Cuál es tu peso actual?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        onClick = { selectedUnit = "Kg"; quantity = maxKgs },
                        modifier = Modifier
                            .width(130.dp)
                            .height(46.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                        )
                    ) {
                        Text(
                            text = "Kg",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        onClick = { selectedUnit = "Lbs"; quantity = maxLbs },
                        modifier = Modifier
                            .width(130.dp)
                            .height(46.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                        )
                    ) {
                        Text(
                            text = "Lbs",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp),
                    textAlign = TextAlign.Center,
                    text = "$selectedValue $selectedUnit",
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.work_sans)
                        ), fontSize = 47.sp, fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Ruler(quantity = quantity, onValueChange = { newValue ->
                    selectedValue = newValue
                    signupViewModel.setWeight(newValue.toFloat())
                })
                Box(modifier = Modifier.padding(bottom = 36.dp)) {

                    PrimaryIconButton(
                        arrow = true,
                        onClick = {
                            signupViewModel.setWeight(selectedValue.toFloat())
                            navController.navigate(HEIGHT_SELECTION)
                        },
                        blackContent = true,
                        text = "Continuar",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

        }
    )
}

@Preview(showBackground = true)
@Composable
fun WeightSelectionPreview() {
    VitaTheme {
       WeightSelectionScreen(signupViewModel = SignupViewModel())
    }
}
