package com.health.vita.main.presentation


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.health.vita.R
import com.health.vita.core.navigation.Screen
import com.health.vita.core.navigation.Screen.PROFILE
import com.health.vita.core.navigation.Screen.PROFILE_EDITION
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.main.presentation.viewmodels.LogOutViewModel
import com.health.vita.meals.data.datastore.DataStoreKeys
import com.health.vita.meals.data.datastore.saveValueAndTimestamp
import com.health.vita.ui.components.general.CustomPopup
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.theme.VitaTheme

@Composable
fun AccountSettingsScreen(
    navController: NavController = rememberNavController(),
    logOutViewModel: LogOutViewModel = viewModel()
) {

    var option by remember { mutableStateOf("") }

    var openLogOutPopup by remember { mutableStateOf(false) }

    var infoConfig by remember { mutableStateOf("") }
    val uiState by logOutViewModel.uiState.observeAsState(UiState.Idle)


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primary)
                        .fillMaxWidth()
                        .height(140.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    GeneralTopBar(
                        text = "Configuración",
                        hasStep = false,
                        lightMode = false,
                        hasIcon = false,
                        onClick = { navController.navigateUp() },
                        onClickIcon = {}
                    )
                }


                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "General",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 25.sp

                        )
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {

                    SettingsOption(
                        iconId = R.drawable.outline_person_24,
                        text = "Información personal",
                        onClick = {
                            option = "Información personal"
                            navController.navigate(PROFILE_EDITION)
                        }
                    )

                    SettingsOption(
                        iconId = R.drawable.baseline_logout_24,
                        text = "Cerrar sesión",
                        onClick = {
                            openLogOutPopup = !openLogOutPopup
                        }
                    )


                    when (uiState){

                        is UiState.Idle -> {

                            infoConfig = "Idle"
                        }

                        is UiState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is UiState.Error -> {
                            infoConfig = "Error " + (uiState as UiState.Error).error.message
                        }
                        is UiState.Success -> {
                            infoConfig = "Success"
                        }
                    }



                }


            }

            CustomPopup(
                showDialog = openLogOutPopup,
                onDismiss = { option = "" },
                title = option,
                height = 0.3f,
                onCancel = { openLogOutPopup = false },
                onConfirm = {
                    logOutViewModel.Logout()
                    openLogOutPopup = false
                    navController.navigate(Screen.LOGIN)

                },
                content = {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "¿Estás seguro de que deseas cerrar sesión?",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            textAlign = TextAlign.Center,
                        )
                    }

                }
            )
        }
    )
}

@Composable
fun SettingsOption(iconId: Int, text: String, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() }
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "selected icon",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
            contentDescription = "arrow",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )

    }
}

@Preview(showBackground = true)
@Composable
fun ConfigurationPreview() {
    VitaTheme {
        AccountSettingsScreen(logOutViewModel = viewModel())
    }
}

