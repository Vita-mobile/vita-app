package com.health.vita.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.health.vita.core.utils.error_management.AppError
import com.health.vita.core.utils.error_management.ErrorManager

@Preview(showBackground = true)
@Composable
fun ErrorPopUp() {

    val errorEvent by ErrorManager.errorLiveData.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    var currentError by remember { mutableStateOf<AppError?>(null) }

    LaunchedEffect(errorEvent) {

        errorEvent?.getContentIfNotHandled()?.let { appError ->

            showDialog = true
            currentError = appError
        }
    }

    if (showDialog && currentError != null) {


        val onDismissRequest: () -> Unit = {

            showDialog = false
            currentError = null
        }

        Dialog(
            onDismissRequest = {

                onDismissRequest()
            }) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {


                    Box(
                        modifier = Modifier.weight(1F),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = "Error en la aplicación",
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        )
                    }

                    Text(
                        modifier = Modifier
                            .weight(4f)
                            .padding(8.dp)
                            .fillMaxWidth(),
                        text = currentError?.message
                            ?: "Ocurrió un error, pero no se encontró mensaje.",
                        style = TextStyle(fontSize = 16.sp)
                    )
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(
                            onClick = { onDismissRequest() }
                        ) {
                            Text(text = "Entendido",

                                style = TextStyle(fontSize = 16.sp)
                                )
                        }
                    }
                }

            }

        }

    }


}









