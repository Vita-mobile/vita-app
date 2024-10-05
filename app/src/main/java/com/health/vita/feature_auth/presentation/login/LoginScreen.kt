package com.health.vita.feature_auth.presentation.login

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import com.health.vita.R
import com.health.vita.ui.theme.Dimens.borderRadius
import com.health.vita.ui.theme.Dimens.paddingScreen


@Composable
fun LoginScreen() {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

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



            CredentialInput(
                "Email",
                email,
                icon = R.drawable.outline_email_24,
                "Email icon",
                onValueChange = { newValue -> email = newValue })

            CredentialInput(
                "Password",
                password, icon = R.drawable.outline_password_24, "Password icon",
                onValueChange = { newValue -> password = newValue },
                isPassword = true
            )
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
                contentDescription = "Button",
            )
            TextField(
                value = value,
                onValueChange = { onValueChange(it) },
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        isFocus = focusState.isFocused
                    }
                    .padding(0.dp)
                    .weight(1F),

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

@Composable
fun LoginButton() {

}
