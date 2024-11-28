package com.health.vita.profile.presentation



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.core.navigation.Screen.EDIT_HEIGHT_SELECTION
import com.health.vita.core.navigation.Screen.EDIT_WEIGHT_SELECTION
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.domain.model.User
import com.health.vita.profile.presentation.viewModel.ProfileViewModel
import com.health.vita.ui.components.general.CustomPopup
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.theme.Cyan
import com.health.vita.ui.theme.LightTurquoise2
import com.health.vita.ui.theme.VitaTheme
import kotlinx.coroutines.delay
import com.health.vita.ui.theme.TranslucentBlue
import com.health.vita.ui.theme.WhiteVariant

@Composable
fun ProfileEditionScreen(navController: NavController = rememberNavController(), profileViewModel: ProfileViewModel = viewModel()) {

    val userState by profileViewModel.user.observeAsState()

    val uiState by profileViewModel.uiState.observeAsState(UiState.Idle)

    var openDataUpdatePopup by remember { mutableStateOf(false) }

    var updateInfo by remember { mutableStateOf("") }


    var email by remember { mutableStateOf("") }

    var name by remember { mutableStateOf("")}

    var lastname by remember { mutableStateOf("")}


    //var emailTouched by remember { mutableStateOf(false) }

    //var isEmailValid by remember { mutableStateOf(true) }

    LaunchedEffect (true){
        profileViewModel.getCurrentUser()
    }

    LaunchedEffect(userState) {
        userState?.let {
            email = it.email
            name = "${it.name} "
            lastname = it.lastName
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            openDataUpdatePopup = true
            delay(2200)
            openDataUpdatePopup = false
            profileViewModel.resetUiState()
        }
    }

    /*fun isEmailFormatValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return email.matches(emailRegex.toRegex())
    }*/

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
                            text = "Perfil detallado",
                            hasStep = false,
                            lightMode = false,
                            hasIcon = true,
                            onClick = { navController.navigateUp() },
                            onClickIcon = {}
                        )
                    }

                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),

                    ) {

                        Box(modifier = Modifier.weight(0.09f))

                        ProfileTextField(value = email, enable = false, label = "Dirección de correo", onValueChange = { newValue ->
                            email = newValue
                            /*emailTouched = true
                            isEmailValid = isEmailFormatValid(email)*/
                        })

                        /*if (emailTouched && !isEmailFormatValid(email)) {
                            Text(
                                "El nuevo formato de correo no es válido. Intente de nuevo",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }*/

                        Box(modifier = Modifier.weight(0.05f))

                        ProfileTextField(value = name, label = "Nombre ", enable = true, onValueChange = { name = it })

                        Box(modifier = Modifier.weight(0.05f))

                        ProfileTextField(value = lastname, label = "Apellido", enable = true, onValueChange = { lastname = it })


                        Box(modifier = Modifier.weight(0.1f))

                        Row (modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF0F0F0),
                             shape = RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceAround){

                            Button(
                                modifier = Modifier.weight(0.08f),
                                shape = RoundedCornerShape(12.dp),
                                onClick = {
                                    navController.navigate(EDIT_HEIGHT_SELECTION)
                                })
                            {
                                Text("ALTURA")

                            }

                            Box(modifier = Modifier.weight(0.01f))

                            Button(modifier = Modifier.weight(0.08f),
                                shape = RoundedCornerShape(12.dp),
                                onClick = {
                                    navController.navigate(EDIT_WEIGHT_SELECTION)
                                }) {

                                Text("PESO")

                            }
                        }

                        Box(modifier = Modifier.weight(0.1f))

                        PrimaryIconButton(
                            text = "Guardar",
                            onClick = {

                                val updatedUser = userState?.copy(
                                    //email = if (email.isEmpty()) userState?.email ?: "" else email,
                                    name = if (name.isEmpty()) userState?.name ?: "" else name.trim(),
                                    lastName = if (lastname.isEmpty()) userState?.lastName ?: "" else lastname.trim()
                                )?: User(
                                    //email = email,
                                    name = name,
                                    lastName = lastname
                                )


                                //if (isEmailFormatValid(updatedUser.email) ) {
                                    profileViewModel.updatePersonalUserData(updatedUser)
                                // }



                            }
                            ,
                            arrow = true,
                            //enabled = isEmailFormatValid(email)
                        )

                        Box(modifier = Modifier.weight(0.5f))

                        when(uiState){

                            is UiState.Idle -> {

                                updateInfo = "Idle"

                            }

                            is UiState.Loading -> {

                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            is UiState.Success -> {

                                updateInfo = "Success"

                            }

                            is UiState.Error -> {

                                updateInfo = "Error " + (uiState as UiState.Error).error.message

                            }
                        }

                        Box(modifier = Modifier.weight(0.5f))

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


@Composable
fun ProfileTextField(value: String, enable: Boolean, label: String, onValueChange: (String) -> Unit) {

    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )

        OutlinedTextField(

            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(19.dp),
            enabled = enable,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).
            border(
                if(isFocused) BorderStroke(5.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)) else BorderStroke(0.dp, Color.Transparent),
            shape = RoundedCornerShape(19.dp)),
            colors = TextFieldDefaults.colors(

                focusedContainerColor = WhiteVariant,
                unfocusedContainerColor = WhiteVariant,
                focusedIndicatorColor = Cyan ,
                unfocusedIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Gray,
                disabledPlaceholderColor =  WhiteVariant,
                disabledLabelColor = Color.Transparent,
                disabledContainerColor = WhiteVariant

            ),

        )
    }

}

    @Preview(showBackground = true)
    @Composable
    fun ConfigurationPreview() {
        VitaTheme {
            ProfileEditionScreen()
        }
    }




