package com.health.vita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.health.vita.auth.presentation.login.LoginScreen
import com.health.vita.core.navigation.NavGraph
import com.health.vita.ui.components.general.PrimaryIconButton
import com.health.vita.ui.theme.Cyan
import com.health.vita.ui.theme.VitaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            VitaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavGraph(navController = navController)

}



@Preview(showBackground = true)
@Composable
fun MyPreview() {
    VitaTheme {
        App()
    }
}

