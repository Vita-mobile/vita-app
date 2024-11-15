package com.health.vita.ui.components.general

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomPopup(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    title: String,
    content: @Composable () -> Unit,
    properties: DialogProperties = DialogProperties(),
    height : Float = 0.5f,
    width  : Float = 1f,
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {}

) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = properties,

        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth(width).fillMaxHeight(height)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    // Pop up title
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )

                    // Pop-up content
                    Box(modifier = Modifier.weight(1f))
                    content()
                    Box(modifier = Modifier.weight(1f))

                    // Action buttons
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Button( onClick = onCancel) {

                            Text(text = "Cancelar")

                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button( onClick = onConfirm) {

                            Text(text = "Confirmar")

                        }


                    }
                }
            }
        }
    }
}
