import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.openqa.selenium.chrome.ChromeDriver
import kotlin.time.Duration.Companion.seconds

class Crawler {
    @Volatile private var running = false

    suspend fun  start(
        chromeDriver: ChromeDriver,
    ) {
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
                    .firstOrNull()
            } ?: return@withContext

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
                    running = true

                    while (running) {
                        runCatching {
                            chromeDriver.autoCapture(image)
                        }.onFailure {
                            it.printStackTrace()
                            ChromeManager.closeSession(chromeDriver)
                            break
                        }

                        delay(5.seconds)
                    }
                }
            }
        }
    }

    suspend fun allStop() {
        withContext(Dispatchers.IO) {
            if (running) {
                ChromeManager.closeAllSessions()
                running = false
            }
        }
    }
}