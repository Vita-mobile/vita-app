package com.health.vita.ui.components.meals

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.R
import com.health.vita.core.navigation.Screen.DIETS_PREVIEW
import com.health.vita.core.navigation.Screen.DIET_SELECTION
import com.health.vita.ui.components.main.CircularIconOutlinedIconButton
import com.health.vita.ui.theme.VitaTheme
import kotlin.math.absoluteValue

@Preview(showBackground = true, widthDp = 420)
@Composable
fun PrevCarr(){
    VitaTheme {
        MealsCarousel(0)
    }
}

val listRes  = listOf(
    R.drawable.cardimg1,
    R.drawable.cardimg2,
    R.drawable.cardimg3,
    R.drawable.cardimg4,
    R.drawable.cardimg5
)

val colors = listOf(
    Color(0xFFcce26e),
    Color(0xFFf4d963),
    Color(0xFF9464cb),
    Color(0xFFa05c96),
    Color(0xFFca4313)
)
@Composable
fun MealsCarousel(meals: Int = 0,
                  navController: NavController = rememberNavController(),
                  currentMeal: Int = 0){
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var dynamicPadding = 0.dp
    if(screenWidth<=427.dp){
        dynamicPadding = (screenWidth*0.3f).coerceAtLeast(40.dp)
    }else if(screenWidth<=1024.dp){
        dynamicPadding = (screenWidth*0.37f).coerceAtLeast(40.dp)
    }

    val pagerState = rememberPagerState(initialPage =  currentMeal,pageCount = {
        meals
    })
    Row{
    }
    if(meals!=0) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = dynamicPadding)
        ) { page ->
            Card(shape = RoundedCornerShape(10.dp), modifier = Modifier.graphicsLayer {
                val pageOffset = calculateCurrentOffsetForPage(page, pagerState.currentPage)
                lerp(
                    start = 0.85f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                ).also { scale ->
                    scaleX = scale
                    scaleY = scale
                }
                alpha = lerp(
                    start = 0.5f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
            }) {
                DietCard(page, navController, currentMeal, meals)

            }
        }
    }else{
        Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { navController.navigate(DIET_SELECTION) }.fillMaxWidth().height(150.dp).background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))){
            Icon(painterResource(id = R.drawable.baseline_add_24), contentDescription = "")
        }
    }
}

fun calculateCurrentOffsetForPage(page: Int, currentPage: Int): Float {
    return (page - currentPage).absoluteValue.toFloat()
}

@Composable
fun DietCard(page: Int, navController: NavController = rememberNavController(), currentMeal: Int, totalMeals: Int){
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(modifier = Modifier
        .background(
            obtenerElementoCircular(colors, (page + screenWidth.value).toInt()) ?: Color.Transparent
        )
        .padding(8.dp)
        .clickable {
            if (currentMeal < totalMeals) {
                if (page == currentMeal) {
                    navController.navigate("DietsPreview/${currentMeal+1}")
                } else if(page > currentMeal){
                    Toast
                        .makeText(
                            navController.context,
                            "Debes completar la comida ${currentMeal + 1} primero",
                            Toast.LENGTH_LONG
                        )
                        .show()
                }else{
                    Toast
                        .makeText(
                            navController.context,
                            "Ya haz completado esta comida, ve a la comida ${currentMeal + 1} para continuar",
                            Toast.LENGTH_LONG
                        )
                        .show()
                }
            }else{
                Toast
                    .makeText(
                        navController.context,
                        "Ya haz completado todas tus comidas de hoy, vuelve mañana",
                        Toast.LENGTH_LONG
                    )
                    .show()
            }
        }) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "Dieta balanceada", color = MaterialTheme.colorScheme.onPrimary)
            CircularIconOutlinedIconButton(R.drawable.baseline_arrow_forward_ios_24, onClick = {}, color = MaterialTheme.colorScheme.onBackground, containerColor = MaterialTheme.colorScheme.onPrimary, border = null, size = 18)
        }
        Row(verticalAlignment = Alignment.CenterVertically ){
            Row(modifier = Modifier.weight(3f)){
                Text(text = "Comida "+(page+1), color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyLarge)
            }
            Row(modifier = Modifier.weight(2f)){
                Image(painter = painterResource(id = obtenerElementoCircular(listRes, (page+screenWidth.value).toInt())?:R.drawable.cardimg5), contentDescription ="diet_img", modifier = Modifier.size(40.dp))
            }
        }
        Text(text = "Siente tu progreso", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.bodyLarge)
    }
}

fun <T> obtenerElementoCircular(lista: List<T>, indice: Int): T? {
    if (lista.isEmpty()) return null  // Retorna null si la lista está vacía
    val indiceCircular = indice % lista.size
    return lista[indiceCircular]
}