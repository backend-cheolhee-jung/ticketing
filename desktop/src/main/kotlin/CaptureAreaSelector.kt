import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun captureAreaSelector(
    onAreaSelected: (Int, Int, Int, Int) -> Unit,
) {
    var startOffset by remember { mutableStateOf<Offset?>(null) }
    var endOffset by remember { mutableStateOf<Offset?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { startOffset = it },
                    onDragEnd = {
                        startOffset?.let { start ->
                            endOffset?.let { end ->
                                val x = minOf(start.x, end.x).toInt()
                                val y = minOf(start.y, end.y).toInt()
                                val width = kotlin.math.abs(start.x - end.x).toInt()
                                val height = kotlin.math.abs(start.y - end.y).toInt()
                                onAreaSelected(x, y, width, height)
                            }
                        }
                        startOffset = null
                        endOffset = null
                    },
                    onDrag = { change, _ ->
                        endOffset = change.position
                    }
                )
            }
    ) {
        if (startOffset != null && endOffset != null) {
            val left = minOf(startOffset!!.x, endOffset!!.x)
            val top = minOf(startOffset!!.y, endOffset!!.y)
            val right = maxOf(startOffset!!.x, endOffset!!.x)
            val bottom = maxOf(startOffset!!.y, endOffset!!.y)

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    color = Color.Red.copy(alpha = 0.3f),
                    topLeft = Offset(left, top),
                    size = Size(right - left, bottom - top)
                )
            }
        }
    }
}
