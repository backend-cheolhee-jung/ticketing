import io.github.bonigarcia.wdm.WebDriverManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

object ChromeManager {
    init {
        WebDriverManager.chromedriver().clearResolutionCache().setup()
    }

    val chromeDrivers = mutableListOf<ChromeDriver>()

    fun closeSession(
        driver: ChromeDriver,
    ) {
        driver.quit()
        chromeDrivers.remove(driver)
    }

    fun closeAllSessions() {
        chromeDrivers.forEach { it.quit() }
        chromeDrivers.clear()
    }

    suspend fun newChrome(
        headless: Boolean = false,
    ) =
        withContext(Dispatchers.IO) {
            val chromeOption = chromeOptions
                .apply { if (headless) addArguments("--headless") }
            ChromeDriver(chromeOption)
                .also {
                    it.executeCdpCommand(
                        "Page.addScriptToEvaluateOnNewDocument",
                        mapOf("source" to "Object.defineProperty(navigator, 'webdriver', { get: () => undefined })")
                    )
                    it.executeCdpCommand(
                        "Network.setUserAgentOverride",
                        mapOf("userAgent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    )
                    chromeDrivers.add(it)
                }
        }

    private val chromeOptions =
        ChromeOptions().apply {
            setBinary(ChromeOption.BINARY)
            addArguments(
                "--start-maximized",
                "--disable-popup-blocking",
                "--disable-notifications",
                "--disable-default-apps",
                "--disable-web-security",
                "--disable-extensions",
                "--disable-infobars",
                "--disable-gpu",
                "--no-sandbox",
                "--disable-blink-features=AutomationControlled",
            )
        }

    object ChromeOption {
        const val BINARY = ""
    }
}