import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import kotlin.time.Duration.Companion.seconds

@Composable
@Preview
fun app() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        app()

        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                val image = CaptureImages.selectAll()
                    .orderBy(CaptureImages.id, SortOrder.DESC)
                    .limit(1)
                    .map(CaptureImage::of)
                    .first()

                val website = Websites.selectAll()
                    .orderBy(Websites.id, SortOrder.DESC)
                    .limit(1)
                    .map(Website::of)
                    .first()

                val chromeDriver = ChromeManager.newChrome()

                withContext(Dispatchers.IO) {
                    with(chromeDriver) {
                        access(website.loginUrl)
                        val idInput = website.idInput.toXPath()
                        val passwordInput = website.passwordInput.toXPath()
                        val loginButton = website.loginButtonElement.toXPath()

                        findElement(idInput).sendKeys(website.email)
                        findElement(passwordInput).sendKeys(website.password)
                        findElement(loginButton).click()

                        access(website.url)

                        while (true) {
                            runCatching {
                                autoCapture(image)
                            }.onFailure {
                                it.printStackTrace()
                                chromeDriver.exit()
                                break
                            }

                            delay(5.seconds)
                        }
                    }
                }
            }
        }
    }
}
