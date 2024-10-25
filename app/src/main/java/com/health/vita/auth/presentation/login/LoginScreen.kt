package com.health.vita.auth.presentation.login

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.health.vita.R
import com.health.vita.core.navigation.Screen
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.ui.theme.Dimens.borderRadius
import com.health.vita.ui.theme.Dimens.paddingScreen


@Composable
fun LoginScreen(
    navController: NavController = rememberNavController(),
    loginViewModel: LoginViewModel = viewModel()
) {




    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var infoLogin by remember {

        mutableStateOf("")
    }

    val scrollState = rememberScrollState()

    val uiState by loginViewModel.uiState.observeAsState(UiState.Idle)

    Scaffold() { innerPadding ->


        Box(
            modifier = Modifier
                .fillMaxHeight(0.38F)
                .fillMaxWidth()


        ) {


            Image(
                painterResource(id = R.drawable.gym_background),
                contentDescription = "Gym background",
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .graphicsLayer(alpha = 0.8F)
                    .padding(innerPadding)
                    .padding(horizontal = 0.dp)
                    .padding(top = paddingScreen),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopEnd

            )


        }

        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.White)

                    )
                )
                .fillMaxHeight(0.38F)
                .fillMaxWidth()

        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = paddingScreen)
                .padding(top = paddingScreen)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxHeight(0.4F)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //APP icon
                Image(
                    painterResource(id = R.drawable.vita_icon),
                    contentDescription = "Vita logo",
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(borderRadius))
                        .size(100.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )

                Box(modifier = Modifier.size(32.dp))

                Text(
                    "Inicia sesión en Vita",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.size(12.dp))

                Text(
                    "¡Vamos a personalizar tu entrenamiento!",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.size(24.dp))
            }

            Column(modifier = Modifier.weight(1F).verticalScroll(scrollState)) {

                //Credential inputs
                CredentialInput(
                    "Correo electrónico",
                    email,
                    icon = R.drawable.outline_email_24,
                    "Email icon",
                    onValueChange = { newValue -> email = newValue })

                Box(modifier = Modifier.size(10.dp))

                CredentialInput(
                    "Contraseña",
                    password, icon = R.drawable.outline_password_24, "Password icon",
                    onValueChange = { newValue -> password = newValue },
                    isPassword = true
                )

                Box(modifier = Modifier.size(10.dp))

                //Log-in button

                Button(
                    onClick = {

                        Log.d("LoginScreen", "Email: $email, Password: $password")

                        val trimmedEmail = email.trim()
                        val trimmedPassword = password.trim()

                        loginViewModel.login(trimmedEmail, trimmedPassword)

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp),
                    enabled = email.isNotEmpty() && password.isNotEmpty()
                ) {
                    if (uiState is UiState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            "Iniciar sesión",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }
                }

                Box(modifier = Modifier.size(2.dp))


                when (uiState) {

                    is UiState.Idle -> {
                        infoLogin = ""
                    }

                    is UiState.Loading -> {
                        infoLogin = ""
                    }

                    is UiState.Success -> {


                        navController.navigate(Screen.PROFILE)

                    }

                    is UiState.Error -> {

                        infoLogin = (uiState as UiState.Error).error.message

                    }


                }

                Text(
                    text = infoLogin,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )


            }


            //Google login

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    "También podés iniciar sesión con:",
                    style = MaterialTheme.typography.bodySmall
                )
                Box(modifier = Modifier.size(12.dp))

                Row(

                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center

                ) {

                    Box(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(borderRadius))
                            .border(
                                4.dp,
                                MaterialTheme.colorScheme.surfaceContainer,
                                shape = RoundedCornerShape(borderRadius)
                            )
                            .clickable { }


                    ) {

                        Image(
                            painterResource(id = R.drawable.google),
                            contentDescription = "Google icon",
                            modifier = Modifier
                                .size(64.dp)
                                .padding(12.dp)


                        )
                    }
                }

                Box(modifier = Modifier.size(12.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {


                        Text(
                            "¿No tienes cuenta? ",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Box(modifier = Modifier.size(12.dp))
                        Text(
                            "Regístrate",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {

                                navController.navigate(Screen.SIGN_UP)

                            })

                    }

                    Box(modifier = Modifier.size(12.dp))

                    Text(
                        "Olvidé mi contraseña",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { /*TODO*/ })

                }

                Box(modifier = Modifier.size(32.dp))


            }


        }
    }
}

@Composable
fun CredentialInput(
    topLabel: String = "none",
    value: String = "",
    icon: Int,
    contentDescription: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false

) {

    var isFocus by remember {
        mutableStateOf(false)
    }

    val borderColor = if (isFocus) {
        MaterialTheme.colorScheme.surfaceContainer

    } else {
        Color.Transparent
    }

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(text = topLabel, style = MaterialTheme.typography.labelMedium)

        Box(modifier = Modifier.size(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
            Modifier
                .clip(shape = RoundedCornerShape(borderRadius))
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    4.dp, color = borderColor, shape = RoundedCornerShape(
                        borderRadius
                    )
                )
                .padding(8.dp)
        ) {

            Box(modifier = Modifier.size(12.dp))

            Icon(
                painter = painterResource(id = icon),
                contentDescription = contentDescription,
            )
            TextField(
                value = value,
                onValueChange = { onValueChange(it) },
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        isFocus = focusState.isFocused
                    }
                    .padding(0.dp)
                    .weight(1F)
                    .height(55.dp),


                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                visualTransformation = if (isPassword && !isPasswordVisible) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },

                )

            //Password logic

            if (isPassword) {

                if (isPasswordVisible) {

                    Icon(
                        painter = painterResource(id = R.drawable.outline_visibility_off_24),
                        contentDescription = "Button",

                        modifier = Modifier
                            .clickable {
                                isPasswordVisible = !isPasswordVisible

                            }
                    )

                } else {


                    Icon(
                        painter = painterResource(id = R.drawable.outline_visibility_24),
                        contentDescription = "Button",
                        modifier = Modifier
                            .clickable {
                                isPasswordVisible = !isPasswordVisible

                            }
                    )
                }

                Box(modifier = Modifier.size(12.dp))

            }


        }


    }
}