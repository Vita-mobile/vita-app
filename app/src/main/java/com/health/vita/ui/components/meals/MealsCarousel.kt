package com.health.vita.ui.components.meals

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.health.vita.R
import com.health.vita.core.navigation.Screen.DIETS_PREVIEW
import com.health.vita.ui.components.main.CircularIconOutlinedIconButton
import kotlin.math.absoluteValue
import androidx.datastore.preferences.core.Preferences import androidx.datastore.preferences.core.stringPreferencesKey import androidx.datastore.preferences.preferencesDataStore
import com.health.vita.ui.theme.VitaTheme

@Preview(showBackground = true, widthDp = 420)
@Composable
fun PrevCarr(){
    VitaTheme {
        MealsCarousel(5)
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
fun MealsCarousel(meals: Int = 0) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var dynamicPadding = 0.dp
    if(screenWidth<=427.dp){
        dynamicPadding = (screenWidth*0.3f).coerceAtLeast(40.dp)
    }else if(screenWidth<=1024.dp){
        dynamicPadding = (screenWidth*0.37f).coerceAtLeast(40.dp)
    }

    val pagerState = rememberPagerState(initialPage =  0,pageCount = {
        meals
    })
    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End){
    }
    HorizontalPager(state = pagerState, contentPadding  = PaddingValues(horizontal = dynamicPadding)) { page ->
        Card(shape = RoundedCornerShape(10.dp), modifier = Modifier.graphicsLayer {
            val pageOffset = calculateCurrentOffsetForPage(page, pagerState.currentPage)
            lerp(start = 0.85f, stop = 1f, fraction = 1f-pageOffset.coerceIn(0f,1f)).also { scale ->
                scaleX = scale
                scaleY = scale
            }
            alpha = lerp(
                start = 0.5f, stop = 1f, fraction = 1f-pageOffset.coerceIn(0f,1f)
            )
        }) {
            DietCard(page)

        }
    }
}

fun calculateCurrentOffsetForPage(page: Int, currentPage: Int): Float {
    return (page - currentPage).absoluteValue.toFloat()
}

@Composable
fun DietCard(page: Int, navController: NavController = rememberNavController()){
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(modifier = Modifier
        .background(
            obtenerElementoCircular(colors, (page + screenWidth.value).toInt()) ?: Color.Transparent
        )
        .padding(8.dp)
        .clickable { navController.navigate(DIETS_PREVIEW) }) {
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