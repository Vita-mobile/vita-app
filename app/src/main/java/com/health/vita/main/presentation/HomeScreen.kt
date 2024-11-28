package com.health.vita.main.presentation


import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.health.vita.R
import com.health.vita.core.navigation.Screen.ACCOUNT_SETTINGS
import com.health.vita.core.navigation.Screen.LOGIN
import com.health.vita.core.navigation.Screen.MEAL_HOME
import com.health.vita.core.navigation.Screen.PROFILE
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.main.presentation.viewmodels.HomeViewModel
import com.health.vita.profile.presentation.viewModel.ProfileViewModel
import com.health.vita.ui.components.main.CardWithTitle
import com.health.vita.ui.components.main.ProfileCard
import com.health.vita.ui.theme.VitaTheme


@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(),
    homeViewModel: HomeViewModel = viewModel()
) {


    val screenState by homeViewModel.uiState.observeAsState(UiState.Idle)
    val profileImage by homeViewModel.profileImageUrl.observeAsState()

    val userState by homeViewModel.user.observeAsState()

    LaunchedEffect(true) {
        Log.e("HomeScreen", "Obteniendo el usuario actual")

        FirebaseAuth.getInstance().currentUser?.let {
            Log.e("HomeScreen", "Current user UID: ${it.uid}")
            homeViewModel.getCurrentUser()
        } ?: run {
            Log.e("HomeScreen", "Current user is null")
            navController.navigate(LOGIN)
        }
        homeViewModel.getCurrentUser()
    }

    LaunchedEffect(userState) {

        if (userState?.id?.isEmpty() == false) {
            Log.e("HomeScreen", "El estado del usuario ha cambiado: $userState")

            homeViewModel.getProfileImage()
        }else{

            homeViewModel.getCurrentUser()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                ProfileCard(
                    name = "${userState?.name}",
                    onClick = { navController.navigate(PROFILE) },
                    onClickButton1 = {},
                    url = profileImage,
                    onClickButton2 = {
                        navController.navigate(
                            ACCOUNT_SETTINGS
                        )
                    })
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(ScrollState(0)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val content: @Composable (color: Color) -> Unit = { color ->
                        Text(
                            text = "Proximamente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = color,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                    CardWithTitle("Entrenamientos", R.drawable.main_sportcard) {
                        content(MaterialTheme.colorScheme.background)
                    }
                    CardWithTitle(
                        "Alimentacion",
                        R.drawable.main_mealcard,
                        { navController.navigate(MEAL_HOME) }) {}
                    CardWithTitle("Entrenador IA", R.drawable.main_iacard) {
                        content(MaterialTheme.colorScheme.background)
                    }
                }
            }
        }
    )

    when (screenState) {
        is UiState.Loading -> {
            Log.e("HomeScreen", "Loading")
        }
        is UiState.Success -> {
            Log.e("HomeScreen", "Success")
        }
        is UiState.Error -> {
            val errorMessage =  (screenState as UiState.Error).error.message
            Log.e("HomeScreen", "Error: $errorMessage")
        }

        is UiState.Idle -> {
            Log.e("HomeScreen", "Idle")
        }


    }



}

@Preview(showBackground = true)
@Composable
fun HomePrev() {
    VitaTheme {
        HomeScreen()
    }
}

