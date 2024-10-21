package com.health.vita.profile.presentation

import GeneralTopBar
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.R
import com.health.vita.ui.components.profile.UserInfoCard
import com.health.vita.ui.components.profile.WeeklyBarChart
import com.health.vita.ui.theme.DarkBlue
import com.health.vita.ui.theme.MainBlue
import com.health.vita.ui.theme.MediumGray
import com.health.vita.ui.theme.White

@Preview
@Composable
fun ProfileScreen(navController: NavController = rememberNavController()) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GeneralTopBar(text = "Regresar", hasStep = false, onClick = { navController.navigateUp() })

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
            text = "Alejandro Londoño Bermúdez",
            color = DarkBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Text(
            text = "Cali, Colombia",
            color = MediumGray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            UserInfoCard(label = "Edad", value = "19 años", modifier = Modifier.weight(1f))
            UserInfoCard(label = "Peso", value = "70 kg", modifier = Modifier.weight(1f))
            UserInfoCard(label = "Altura", value = "185 cm", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        UserInfoCard(label = "Ingesta diaria de kcal", value = "2500 kcal", modifier = Modifier.fillMaxWidth())
    }
}
