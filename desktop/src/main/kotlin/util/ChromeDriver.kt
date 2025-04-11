package util

import config.ChromeManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration


suspend inline fun <reified T : ChromeDriver> T.exit() =
    withContext(IO) {
        ChromeManager.chromeDrivers.remove(this@exit)
        quit()
    }

suspend fun ChromeDriver.access(
    url: String,
) =
    withContext(IO) {
        try {
            get(url)
        } catch (e: Exception) {
            exit()
        }
    }

suspend fun RemoteWebDriver.hasElement(
    by: By,
) =
    withContext(IO) {
        try {
            findElement(by)
            true
        } catch (e: Exception) {
            false
        }
    }

suspend fun RemoteWebDriver.waitUntilElementRendered(
    element: By,
    waitTime: Duration = 5.seconds,
) {
    withContext(IO) {
        val wait = WebDriverWait(this@waitUntilElementRendered, waitTime.toJavaDuration())
        wait.until(ExpectedConditions.visibilityOfElementLocated(element))
    }
}

suspend fun RemoteWebDriver.isElementRendered(
    element: By,
    waitTime: Duration = 5.seconds,
) =
    withContext(IO) {
        try {
            val wait = WebDriverWait(this@isElementRendered, waitTime.toJavaDuration())
            wait.until(ExpectedConditions.visibilityOfElementLocated(element))
            true
        } catch (e: Exception) {
            false
        }
    }

suspend fun RemoteWebDriver.waitUntilElementRemoved(
    element: By,
    waitTime: Duration = 5.seconds,
) {
    withContext(IO) {
        val wait = WebDriverWait(this@waitUntilElementRemoved, waitTime.toJavaDuration())
        wait.until { driver ->
            val elements = driver.findElements(element)
            elements.isEmpty() || elements.all { !it.isDisplayed }
        }
    }
}

suspend fun ChromeDriver.doubleClick(
    button: WebElement,
) {
    withContext(IO) {
        toJavascriptExecutor().executeScript(CLICK, button)
        toJavascriptExecutor().executeScript(CLICK, button)
    }
}

fun RemoteWebDriver.toJavascriptExecutor() =
    this as JavascriptExecutor

private const val CLICK = "arguments[0].click()"