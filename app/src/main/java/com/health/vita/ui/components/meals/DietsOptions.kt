package com.health.vita.ui.components.meals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DietsOptions(
    dietType: String,
    meal: Int,
    navController: NavController
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        listOf("Favoritas", "Mi plan", "Creaciones").forEach { option ->
            val isSelected = dietType == option
            Text(
                text = option,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                ),
                modifier = Modifier
                    .clickable {
                        val route = when (option) {
                            "Favoritas" -> "FavoritesDietsPreview/$meal"
                            "Mi plan" -> "IADietsPreview/$meal"
                            "Creaciones" -> "CreationsDietsPreview/$meal"
                            else -> ""
                        }
                        navController.navigate(route)
                    }
                    .padding(8.dp)
            )
        }
    }
}