import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.apache.commons.mail.SimpleEmail

private val mailSender = ApacheSimpleMailSender(SimpleEmail())

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
            mailSender.send(
                Mail(
                    to = "ekxk1234@gmail.com",
                    subject = "이미지 바꼈습니다.",
                    body = "이미지 바꼈습니다.",
                )
            )
        }
    }
}
