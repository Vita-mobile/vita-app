package com.health.vita.ui.components.general

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.health.vita.R
import com.health.vita.ui.theme.Dimens.borderRadius

@Composable
fun CredentialInput(
    topLabel: String = "none",
    value: String = "",
    icon: Int,
    contentDescription: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default

) {

    var isFocus by remember {
        mutableStateOf(false)
    }

    val borderColor = if (isFocus) {
        MaterialTheme.colorScheme.surfaceContainer

    } else {
        Color.Transparent
    }

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier.fillMaxWidth()) {

        Text(text = topLabel, style = MaterialTheme.typography.labelMedium)

        Box(modifier = Modifier.size(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
            Modifier
                .clip(shape = RoundedCornerShape(borderRadius))
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    4.dp, color = borderColor, shape = RoundedCornerShape(
                        borderRadius
                    )
                )
                .padding(8.dp)
        ) {

            Box(modifier = Modifier.size(12.dp))

            Icon(
                painter = painterResource(id = icon),
                contentDescription = contentDescription,
            )
            TextField(
                value = value,
                onValueChange = { onValueChange(it) },
                keyboardOptions = keyboardOptions,
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        isFocus = focusState.isFocused
                    }
                    .padding(0.dp)
                    .weight(1F)
                    .height(50.dp)

                ,


                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                visualTransformation = if (isPassword && !isPasswordVisible) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },

                )

            //Password logic

            if (isPassword) {

                if (isPasswordVisible) {

                    Icon(
                        painter = painterResource(id = R.drawable.outline_visibility_off_24),
                        contentDescription = "Button",

                        modifier = Modifier
                            .clickable {
                                isPasswordVisible = !isPasswordVisible

                            }
                    )

                } else {


                    Icon(
                        painter = painterResource(id = R.drawable.outline_visibility_24),
                        contentDescription = "Button",
                        modifier = Modifier
                            .clickable {
                                isPasswordVisible = !isPasswordVisible

                            }
                    )
                }

                Box(modifier = Modifier.size(12.dp))

            }


        }


    }
}
