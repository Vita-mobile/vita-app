package com.health.vita.profile.presentation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.R
import com.health.vita.core.navigation.Screen.ACCOUNT_SETTINGS
import com.health.vita.core.navigation.Screen.WELCOME_SCREEN
import com.health.vita.profile.presentation.viewModel.ProfileViewModel
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.profile.UserInfoCard
import com.health.vita.ui.components.profile.WeeklyBarChart
import com.health.vita.ui.theme.DarkBlue
import com.health.vita.ui.theme.MainBlue
import com.health.vita.ui.theme.MediumGray
import com.health.vita.ui.theme.White

@Preview
@Composable
fun ProfileScreen(navController: NavController = rememberNavController(), profileViewModel: ProfileViewModel = viewModel()) {
    val userState by profileViewModel.user.observeAsState()

    Log.e(">>> User", userState.toString())

    LaunchedEffect(true) {
        profileViewModel.getCurrentUser()
    }

    fun calculateCalc(): Double {
        var cal = 0.0
        val level = when (userState?.physicalLevel){
            1 -> 1.2
            2 -> 1.375
            3 -> 1.55
            4 -> 1.725
            5 -> 1.9
            else -> 1.2
        }

        if (userState?.sex == "Masculino"){
            cal = (10.0 * (userState?.weight ?: 0f)) +(6.25 * (userState?.height ?: 0f)) - (5 * (userState?.age ?: 0)) + 5
        } else if (userState?.sex == "Femenino") {
            cal = (10.0 * (userState?.weight ?: 0f)) +(6.25 * (userState?.height ?: 0f)) - (5 * (userState?.age ?: 0)) - 161
        }
        return (cal * level)
    }

    if(userState == null) {
        navController.navigate(WELCOME_SCREEN)
    }else {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GeneralTopBar(
                    text = "Perfil",
                    hasStep = false,
                    lightMode = true,
                    hasIcon = true,
                    onClick = { navController.navigateUp() },
                    onClickIcon = {navController.navigate(ACCOUNT_SETTINGS)}
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MainBlue)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${userState?.name} ${userState?.lastName} " ?: "Desconocido",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                WeeklyBarChart(
                    data = listOf(
                        "Lun" to 50f,
                        "Mar" to 70f,
                        "Mié" to 30f,
                        "Jue" to 90f,
                        "Vie" to 60f,
                        "Sáb" to 40f,
                        "Dom" to 80f
                    ),
                    currentDay = "Mié"
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    UserInfoCard(label = "Edad", value = "${userState?.age} años", modifier = Modifier.weight(1f))
                    UserInfoCard(label = "Peso", value = "${userState?.weight} kg", modifier = Modifier.weight(1f))
                    UserInfoCard(label = "Altura", value = "${userState?.height} cm", modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                UserInfoCard(
                    label = "Ingesta diaria de kcal",
                    value = "${calculateCalc().toInt()}",
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }
}
