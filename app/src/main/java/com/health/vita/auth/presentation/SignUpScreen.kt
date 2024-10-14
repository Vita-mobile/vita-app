package com.health.vita.auth.presentation

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.auth.presentation.viewmodel.SignupViewModel

@Composable
fun SignUpScreen(navController: NavController = rememberNavController(), signupViewModel: SignupViewModel) {
    val name = signupViewModel.name.value?:""
    val email = signupViewModel.email.value?:""
    val password = signupViewModel.email.value?:""
    TextField(
        value = password,
        onValueChange = { pass -> signupViewModel.setName(pass) },
        visualTransformation = PasswordVisualTransformation()
    )
    TextField(
        value = email,
        onValueChange = { mail -> signupViewModel.setName(mail) },
        visualTransformation = PasswordVisualTransformation()
    )
    TextField(
        value = name,
        onValueChange = { nameV -> signupViewModel.setName(nameV) },
        visualTransformation = PasswordVisualTransformation()
    )
}
