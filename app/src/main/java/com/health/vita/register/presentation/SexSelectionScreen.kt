package com.health.vita.register.presentation

import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.register.presentation.viewmodel.SignupViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


import com.health.vita.R
import com.health.vita.core.navigation.Screen
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.theme.VitaTheme

@Composable
fun SexSelectionScreen(navController: NavController = rememberNavController(), signupViewModel: SignupViewModel) {

    val gender by signupViewModel.gender.observeAsState("")

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

                GeneralTopBar(text = "Valoración", step = 5, total = 6, onClick = { navController.navigateUp() })

                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¿Cuál es tu género?",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                }

                Spacer(modifier = Modifier.height(130.dp))

                Column {

                    GenderSelectionCard(
                        text = "Masculino",
                        imageId = R.drawable.male_image,
                        selected = gender == "Masculino",
                        onClick = {signupViewModel.setGender("Masculino")
                        
                        }
                    )

                    GenderSelectionCard(
                        text = "Femenino",
                        imageId = R.drawable.female_image,
                        selected = gender == "Femenino",
                        onClick = { signupViewModel.setGender("Femenino") }
                    )

                }


                Spacer(modifier = Modifier.height(150.dp))

                PrimaryIconButton(
                    text = "Continuar",

                    onClick = {

                        if(gender.isNotEmpty()){

                            navController.navigate(Screen.FITNESS_GOAL_SELECTION)

                        }else{

                            Toast.makeText(navController.context, "Realiza la selección de uno de los dos campos", Toast.LENGTH_LONG).show()

                        }

                    }
                    ,
                    arrow = true
                )
            }
        }
    )
}



@Composable
fun GenderSelectionCard(text: String, imageId: Int, selected: Boolean, onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(165.dp)
            .padding(vertical = 8.dp),
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
        border = if (selected) BorderStroke(5.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)) else null,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F4)),

        ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(15.dp)) {

                Row{

                    Icon(
                        painter = painterResource(id = if (text == "Masculino") {
                            R.drawable.male_24
                        } else {
                            R.drawable.female_24
                        }),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.padding(6.dp))

                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                }

                Spacer(modifier = Modifier.height(50.dp))


                Icon(
                    painter = painterResource(
                        id = if (selected) {R.drawable.outline_radio_button_checked_24} else {R.drawable.outline_radio_button_unchecked_24}
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

            }


            Column {

                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight(1f)
                        .clip(RoundedCornerShape(35.dp)),
                )

            }


        }



    }
}


@Preview(showBackground = true)
@Composable
fun SexSelectionPreview() {
    VitaTheme {
        SexSelectionScreen(signupViewModel = SignupViewModel())
    }
}
