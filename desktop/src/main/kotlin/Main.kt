import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.Duration.Companion.seconds

fun main() {
    DatabaseFactory.connect()
    application {
        var demonProcess by remember { mutableStateOf(DemonProcess.UNREGISTER) }

        Window(onCloseRequest = ::exitApplication) {
            var websiteName by remember { mutableStateOf<String?>(null) }
            var isDialogOpen by remember { mutableStateOf(false) }

            websiteName = transaction {
                Websites.selectAll()
                    .orderBy(Websites.id, SortOrder.DESC)
                    .limit(1)
                    .map(Website::of)
                    .firstOrNull()
                    ?.name
            }

            MaterialTheme {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (websiteName == null) Text("등록된 웹사이트가 없습니다.")
                    else Text("등록된 웹사이트: $websiteName")

                    Spacer(Modifier.height(8.dp))

                    Button(onClick = { isDialogOpen = true }) {
                        Text("새로 등록")
                    }
                }

                if (isDialogOpen) {
                    websiteRegisterDialog(
                        onDismiss = { isDialogOpen = false },
                        onSubmit = {
                            websiteName =
                                transaction {
                                    Websites.selectAll()
                                        .orderBy(Websites.id, SortOrder.DESC)
                                        .limit(1)
                                        .map(Website::of)
                                        .first()
                                        .name
                                }
                            isDialogOpen = false
                            demonProcess = DemonProcess.REGISTER
                        }
                    )
                }
            }

            LaunchedEffect(demonProcess) {
                if (demonProcess == DemonProcess.UNREGISTER) return@LaunchedEffect

                withContext(Dispatchers.IO) {
                    val image = transaction {
                        CaptureImages.selectAll()
                            .orderBy(CaptureImages.id, SortOrder.DESC)
                            .limit(1)
                            .map(CaptureImage::of)
                            .firstOrNull()
                    } ?: return@withContext

                    val website = transaction {
                        Websites.selectAll()
                            .orderBy(Websites.id, SortOrder.DESC)
                            .limit(1)
                            .map(Website::of)
                            .first()
                    }

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
                                    autoCapture(chromeDriver, image)
                                }.onFailure {
                                    it.printStackTrace()
                                    ChromeManager.closeSessions()
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
}
