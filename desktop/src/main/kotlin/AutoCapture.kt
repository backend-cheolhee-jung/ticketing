import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.apache.commons.mail.SimpleEmail
import org.openqa.selenium.chrome.ChromeDriver

private val mailSender = ApacheSimpleMailSender(SimpleEmail())

suspend fun autoCapture(
    chromeDriver: ChromeDriver,
    oldImage: CaptureImage,
) {
    chromeDriver.navigate().refresh()

    val newImage = CaptureImage.of(
        oldImage.x,
        oldImage.y,
        oldImage.width,
        oldImage.height,
    )

    newImage.capture()

    if (oldImage == newImage) return
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

private suspend fun disableNotification(
    chrome: ChromeDriver,
) {
    withContext(IO) {
        with(chrome) {
            val startTime = System.currentTimeMillis()

            while (windowHandles.size < 2) {
                if (startTime.isOverOneSecond()) return@withContext
                delay(100)
            }

            if (windowHandles.size > 1) {
                switchTo().window(windowHandles.last()).close()
                switchTo().window(windowHandles.first())
            }
        }
    }
}

private fun Long.isOverOneSecond() = System.currentTimeMillis() - this > 1000L