package com.health.vita.auth.presentation

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.R
import com.health.vita.ui.components.general.CredentialInput
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.theme.Dimens.borderRadius
import com.health.vita.ui.theme.Dimens.paddingScreen

@Preview
@Composable
fun SignUpScreen(navController: NavController = rememberNavController()) {

    var fullName by remember {
        mutableStateOf("")
    }

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
                    "Registrate",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.size(12.dp))

                Text(
                    "¡Hoy es el momento!",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.size(24.dp))
            }

            Column (modifier = Modifier.weight(1F)){

                //Credential inputs

                CredentialInput(
                    "Nombre",
                    fullName,
                    icon = R.drawable.outline_person_24,
                    "Name icon",
                    onValueChange = { newValue -> fullName = newValue })

                Box(modifier = Modifier.size(20.dp))

                CredentialInput(
                    "Correo electrónico",
                    email,
                    icon = R.drawable.outline_email_24,
                    "Email icon",
                    onValueChange = { newValue -> email = newValue })

                Box(modifier = Modifier.size(20.dp))

                CredentialInput(
                    "Contraseña",
                    password, icon = R.drawable.outline_password_24, "Password icon",
                    onValueChange = { newValue -> password = newValue },
                    isPassword = true
                )

                Box(modifier = Modifier.size(20.dp))

                //Register button


                PrimaryIconButton(
                    arrow = true,
                    blackContent = false,
                    text = "Registrarse",
                    enabled = fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
                )

            }



            //Google register

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    "También podés registrarte con:",
                    style = MaterialTheme.typography.bodySmall
                )
                Box(modifier = Modifier.size(20.dp))

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
                            "¿Ya tienes cuenta? ",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Box(modifier = Modifier.size(12.dp))
                        Text(
                            "Inicia sesión",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { /*TODO*/ })
                    }


                }

                Box(modifier = Modifier.size(32.dp))


            }




        }
    }

}
