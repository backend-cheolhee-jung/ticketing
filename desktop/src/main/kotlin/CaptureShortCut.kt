import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*

@Composable
fun captureShortcut() {
    var isCaptureMode by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
            .onPreviewKeyEvent { event ->
                val isKeyDown = event.type == KeyEventType.KeyDown
                val isCKeyword = event.key == Key.C

                if (isKeyDown && isCKeyword && event.isCtrlPressed && event.isShiftPressed) {
                    isCaptureMode = !isCaptureMode
                    true
                } else {
                    false
                }
            }
            .focusable()
    ) {
        if (isCaptureMode) {
            captureAreaSelector { x, y, w, h ->
                val image = CaptureImage.of(x, y, w, h)
                image.capture()
                image.saveImage()
                isCaptureMode = false
            }
        } else {
            Text("Ctrl + Shift + C 를 눌러 캡쳐 모드를 실행하세요.")
        }
    }
}
