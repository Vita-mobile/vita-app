package com.health.vita.main.presentation


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.health.vita.R
import com.health.vita.core.utils.DatabaseNames
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.register.presentation.FitnessLevelSelectionScreen
import com.health.vita.register.presentation.ObjectiveSelectionCard
import com.health.vita.register.presentation.viewmodel.SignupViewModel
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.theme.VitaTheme

@Composable
fun AccountSettingsScreen(navController : NavController = rememberNavController(), signupViewModel: SignupViewModel = viewModel()) {

    val physicalTarget by remember { mutableStateOf("") }


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

                GeneralTopBar(
                    text = "Valoración",
                    step = 6,
                    total = 6,
                    onClick = { navController.navigateUp() })

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
                        selected = physicalTarget == DatabaseNames.physicalTarget[1],
                        onClick = { signupViewModel.setGoal( DatabaseNames.physicalTarget[1]?:"") }
                    )

                    ObjectiveSelectionCard(
                        text = "Probar el coach de IA",
                        iconId = R.drawable.outline_on_device_training_24,
                        selected = physicalTarget == DatabaseNames.physicalTarget[2],
                        onClick = { signupViewModel.setGoal( DatabaseNames.physicalTarget[2]?:"") }
                    )

                    ObjectiveSelectionCard(
                        text = "Ganar masa muscular",
                        iconId = R.drawable.round_fitness_center_24,
                        selected = physicalTarget == DatabaseNames.physicalTarget[3],
                        onClick = { signupViewModel.setGoal(DatabaseNames.physicalTarget[3]?:"") }
                    )

                    ObjectiveSelectionCard(
                        text = "Mejorar mi alimentación",
                        iconId = R.drawable.rounded_monitor_heart_24,
                        selected = physicalTarget == DatabaseNames.physicalTarget[4],
                        onClick = { signupViewModel.setGoal(DatabaseNames.physicalTarget[4]?:"") }
                    )
                }

                Spacer(modifier = Modifier.height(130.dp))

                PrimaryIconButton(
                    text = "Comenzar",
                    onClick = {
                        if (physicalTarget.isNotEmpty()) {

                            signupViewModel.setGoal(physicalTarget)
                            signupViewModel.registerOperation()
                        } else {

                            Toast.makeText(
                                navController.context,
                                "Realiza la selección de uno de los dos campos",
                                Toast.LENGTH_LONG
                            ).show()


                        }
                    },
                    arrow = true
                )

            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ConfigurationPreview() {
    VitaTheme {
        AccountSettingsScreen(signupViewModel = viewModel())
    }
}

