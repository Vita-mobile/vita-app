package com.health.vita.profile.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
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
import com.health.vita.core.navigation.Screen.PROFILE_EDITION
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.domain.model.User
import com.health.vita.profile.presentation.viewModel.ProfileViewModel
import com.health.vita.ui.components.general.CustomPopup
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.components.profile.Ruler
import com.health.vita.ui.theme.VitaTheme
import kotlinx.coroutines.delay

@Composable
fun EditHeightScreen(
    navController: NavController = rememberNavController(),
    profileViewModel: ProfileViewModel = viewModel()) {

    LaunchedEffect(Unit) {
        profileViewModel.getCurrentUser()
    }

    val maxCm = 240

    val userState by profileViewModel.user.observeAsState()
    val uiState by profileViewModel.uiState.observeAsState(UiState.Idle)


    val height by profileViewModel.height.observeAsState()
    var selectedValue by remember { mutableStateOf(height?:160) }

    var quantity by remember { mutableStateOf(maxCm) }


    var updateInfo by remember { mutableStateOf("") }
    var openDataUpdatePopup by remember { mutableStateOf(false) }


    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            openDataUpdatePopup = true
            delay(2200)
            openDataUpdatePopup = false
            profileViewModel.resetUiState()
            navController.navigateUp()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primary),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)) {

                    GeneralTopBar(
                        text = "",
                        hasStep = false,
                        onClick = { navController.navigateUp() },
                        lightMode = false,
                    )
                    Box(modifier = Modifier.weight(0.2f))

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
                        text = "$height Cm",
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.work_sans)
                            ), fontSize = 47.sp, fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Box(modifier = Modifier.weight(1f))
                    Ruler(quantity = quantity, initialInt = (height?:160).toInt(), onValueChange = { newValue ->
                        selectedValue = newValue
                        profileViewModel.setHeight(newValue.toFloat())

                    })

                    Box(modifier = Modifier.weight(1f))

                    Box(modifier = Modifier.padding(bottom = 36.dp)) {

                        PrimaryIconButton(
                            arrow = true,
                            onClick = {

                                val updatedUser = userState?.copy(height = selectedValue.toFloat())
                                    ?: User(height = selectedValue.toFloat())

                                profileViewModel.updatePersonalUserData(updatedUser)

                            },
                            blackContent = true,
                            text = "Guardar",
                            color = MaterialTheme.colorScheme.onPrimary,
                            isLoading = uiState is UiState.Loading
                        )


                        when(uiState){

                            is UiState.Idle -> {

                                updateInfo = "Idle"

                            }

                            is UiState.Loading -> {

                                updateInfo = "loading"

                            }

                            is UiState.Success -> {

                                updateInfo = "Success"

                            }

                            is UiState.Error -> {

                                updateInfo = "Error " + (uiState as UiState.Error).error.message

                            }
                        }

                    }
                }
            }

            CustomPopup(
                showDialog = openDataUpdatePopup,
                onDismiss = { openDataUpdatePopup = false },
                title = "¡Operación exitosa!" ,
                height = 0.3f,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Success",
                        modifier = Modifier.size(70.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                },
                content = {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Datos actualizados correctamente",
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

@Preview(showBackground = true)
@Composable
fun EditHeightPreview() {
    VitaTheme {
        EditHeightScreen(profileViewModel = ProfileViewModel())
    }
}
