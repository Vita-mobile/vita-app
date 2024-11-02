package com.health.vita.meals.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.ui.theme.VitaTheme

@Composable
fun MealHomeScreen(navController: NavController = rememberNavController()) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)){
            Column(modifier = Modifier.padding(horizontal = 16.dp)){
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(text = "Comidas", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Detalles", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.alpha(0.8f))

                    }
                }
            }

        }



        }
    )
}


@Composable
@Preview(showBackground = true)
fun mealhomeprev() {
    VitaTheme {
        MealHomeScreen()
    }
}