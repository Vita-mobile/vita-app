package com.health.vita.meals.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.meals.domain.model.Ingredient
import com.health.vita.ui.components.general.GeneralTopBar
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.components.meals.BorderLabelText
import com.health.vita.ui.theme.MintGreen

@Preview
@Composable
fun DietSelectionScreen(navController: NavController = rememberNavController()) {
    Scaffold(modifier = Modifier.fillMaxSize(), content = { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GeneralTopBar(
                text = "Nutrición",
                hasStep = false,
                onClick = { navController.navigateUp() })

            Text(
                modifier = Modifier.padding(
                    top = 12.dp,
                    bottom = 16.dp,
                    start = 24.dp,
                    end = 24.dp
                ),
                text = "Te ayudaremos a crear un plan de comidas solo para ti.",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
            BorderLabelText(text = "Leche")
            BorderLabelText(
                text = "Leche",
                border = false,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
            BorderLabelText(text = "Leche")

            PrimaryIconButton(text = "Continuar", color = MintGreen)
        }
    })
}