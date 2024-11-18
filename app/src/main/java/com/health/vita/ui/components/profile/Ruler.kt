package com.health.vita.ui.components.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.vita.R
import kotlinx.coroutines.flow.distinctUntilChanged


@Composable
fun Ruler(
    color: Color = MaterialTheme.colorScheme.onPrimary,
    quantity: Int = 200,
    onValueChange: (Int) -> Unit,
    initialInt: Int = quantity/2
) {
    var selectedValue by remember { mutableStateOf(initialInt) }
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = selectedValue-5)

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }.distinctUntilChanged()
            .collect { index ->
                selectedValue = index + 5
                onValueChange(selectedValue)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        LazyRow(
            state = lazyListState, modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            items(quantity) { index ->
                val value = index + 1

                Column(
                    modifier = Modifier
                        .width(45.dp)
                        .height(120.dp)
                        .padding(horizontal = 4.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (value % 5 == 0) {
                            Text(
                                text = value.toString(), style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.work_sans)),
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = color
                                )
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .weight(1f)
                    ) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)

                        ) {
                            val lineHeight = if (value % 5 == 0) 100f else 30f
                            val centerY = size.height / 2
                            val startY = centerY + lineHeight / 2
                            val endY = centerY - lineHeight / 2
                            drawLine(

                                color = color, start = androidx.compose.ui.geometry.Offset(
                                    x = size.width / 2, y = startY
                                ), end = androidx.compose.ui.geometry.Offset(
                                    x = size.width / 2, y = endY
                                ), strokeWidth = 2.dp.toPx(), cap = StrokeCap.Round
                            )
                        }
                    }

                }
            }
        }
    }
}