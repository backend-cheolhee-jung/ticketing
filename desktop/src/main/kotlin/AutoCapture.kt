import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun autoCapture(
    oldImage: CaptureImage,
) {
    LaunchedEffect(Unit) {
        val newImage = CaptureImage.of(
            oldImage.x,
            oldImage.y,
            oldImage.width,
            oldImage.height,
        )

        newImage.capture()

        if (oldImage == newImage) return@LaunchedEffect
        else {
            oldImage.deleteImage()
            newImage.saveImage()
//            send mail
        }
    }
}
