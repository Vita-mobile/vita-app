package com.health.vita.register.presentation

import GeneralTopBar
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.health.vita.R
import com.health.vita.core.utils.error_management.AppError
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.register.presentation.viewmodel.SignupViewModel

import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.theme.VitaTheme

@Composable
fun FitnessGoalSelectionScreen(navController: NavController = rememberNavController(), signupViewModel: SignupViewModel) {

    val selectedObjective by signupViewModel.goal.observeAsState("")
    val uiState by signupViewModel.uiState.observeAsState(UiState.Idle)

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
                Box(modifier = Modifier.width(16.dp))

                GeneralTopBar(text = "Valoración", step = 6, total = 6, onClick = { navController.navigateUp() })

                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¿Cuál es tu objetivo?",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(130.dp))

                Column {
                    ObjectiveSelectionCard(
                        text = "Perder peso",
                        iconId = R.drawable.rounded_monitor_weight_24,
                        selected = selectedObjective == "Perder peso",
                        onClick = { signupViewModel.setGoal("Perder peso") }
                    )

                    ObjectiveSelectionCard(
                        text = "Probar el coach de IA",
                        iconId = R.drawable.outline_on_device_training_24,
                        selected = selectedObjective == "Probar el coach de IA",
                        onClick = { signupViewModel.setGoal("Probar el coach de IA") }
                    )

                    ObjectiveSelectionCard(
                        text = "Ganar masa muscular",
                        iconId = R.drawable.round_fitness_center_24,
                        selected = selectedObjective == "Ganar masa muscular",
                        onClick = { signupViewModel.setGoal("Ganar masa muscular") }
                    )

                    ObjectiveSelectionCard(
                        text = "Mejorar mi alimentación",
                        iconId = R.drawable.rounded_monitor_heart_24,
                        selected = selectedObjective == "Mejorar mi alimentación",
                        onClick = { signupViewModel.setGoal("Mejorar mi alimentación") }
                    )
                }

                Spacer(modifier = Modifier.height(130.dp))

                PrimaryIconButton(
                    text = "Comenzar",
                    onClick = {
                        if (selectedObjective.isNotEmpty()) {
                            signupViewModel.registerOperation()
                        } else {

                            Toast.makeText(navController.context, "Realiza la selección de uno de los dos campos", Toast.LENGTH_LONG).show()


                        }
                    },
                    arrow = true
                )

                when (uiState) {

                    is UiState.Idle -> {

                    }

                    is UiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is UiState.Success -> {
                        SuccessIndicator()
                    }
                    is UiState.Error -> {
                        val error = (uiState as UiState.Error).error
                        ErrorDialog(error = error)
                    }
                }
            }
        }
    )
}

@Composable
fun ObjectiveSelectionCard(text: String, iconId: Int, selected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        border = if (selected) BorderStroke(4.dp, Color(0xFFC8E6F7)) else null,
        colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.primary else
            Color(0xFFF3F3F4)),
        modifier = Modifier
            .fillMaxWidth()
            .height(83.dp)
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }



            Icon(
                painter = painterResource(id = if (selected) R.drawable.outline_radio_button_checked_24 else R.drawable.outline_radio_button_unchecked_24),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun SuccessIndicator() {
    AlertDialog(
        onDismissRequest = { TODO() },
        title = { Text(text = "Éxito") },
        text = { Text(text = "La operación se completó exitosamente.") },
        confirmButton = {
            TextButton(onClick = { TODO() }) {
                Text("OK")
            }
        }
    )
}

@Composable
fun ErrorDialog(error: AppError) {
    AlertDialog(
        onDismissRequest = { TODO() },
        title = { Text(text = "Error") },
        text = { Text(text = error.message) },
        confirmButton = {
            TextButton(onClick = { TODO() }) {
                Text("OK")
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun FitnessSelectionPreview() {
    VitaTheme {
        FitnessGoalSelectionScreen(signupViewModel = SignupViewModel())
    }
}