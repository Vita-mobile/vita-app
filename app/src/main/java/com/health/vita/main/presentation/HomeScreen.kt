package com.health.vita.main.presentation


import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.health.vita.R
import com.health.vita.ui.components.main.CardWithTitle
import com.health.vita.ui.components.main.ProfileCard
import com.health.vita.ui.theme.VitaTheme


@Composable
fun HomeScreen(navController: NavController = rememberNavController()) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {

                ProfileCard()
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(ScrollState(0))
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CardWithTitle("Entrenamientos", R.drawable.main_sportcard){
                        Text(
                            text = "Proximamente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                    CardWithTitle("Alimentacion",R.drawable.main_mealcard){
                        Text(
                            text = "Proximamente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }

                    CardWithTitle("Entrenador IA", R.drawable.main_iacard){
                        Text(
                            text = "Proximamente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun homePrev(){
    VitaTheme {
        HomeScreen()
    }
}
