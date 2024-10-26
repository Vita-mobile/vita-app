import android.icu.text.Transliterator.Position
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.health.vita.ui.theme.Black
import com.health.vita.ui.theme.LightGray
import com.health.vita.ui.theme.MainBlue
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun activitySlider(
    modifier: Modifier = Modifier,
    initialValue: Int,
    primaryColor: Color,
    secondaryColor: Color,
    minValue: Int = 1,
    maxValue: Int = 5,
    circleRadius: Float,
    onPositionChage: (Int) -> Unit
) {
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var postionValue by remember {
        mutableStateOf(initialValue)
    }

    Box(
        modifier = Modifier
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {

        }
    }

}

@Preview(showBackground = true)
@Composable
fun preview() {
    activitySlider(
        modifier = Modifier
            .size(250.dp)
            .background(LightGray),
        initialValue = 1,
        primaryColor = MainBlue,
        secondaryColor = MainBlue,
        circleRadius = 230f
    ) {
    }
}