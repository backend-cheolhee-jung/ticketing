package com.example.util.extension

import com.example.util.ChromeManager
import com.example.util.constant.JavaScriptEvent.CLICK
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.UnhandledAlertException
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.MILLIS

private val logger = KotlinLogging.logger { }

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
            logger.error(e) { "$url 사이트가 죽었어요" }
        }
    }

suspend fun <T> ChromeDriver.switchToIframe(
    iframeId: String = "Contents",
    duration: Duration = Duration.of(1, MILLIS),
    block: suspend RemoteWebDriver.() -> T,
): T {
    return withContext(IO) {
        manage().timeouts().implicitlyWait(duration)
        switchTo().frame(iframeId)

        try {
            block().apply {
                runCatching {
                    switchTo().alert().dismiss()
                }
                switchTo().defaultContent()
            }
        } catch (e: UnhandledAlertException) {
            runCatching {
                switchTo().alert().dismiss()
            }.onFailure {
                logger.error { "알림창이 없어요" }
            }
            switchToIframe(iframeId, duration, block)
        } catch (e: Exception) {
            exit()
            logger.error(e) { "iframe 작업 중 문제가 발생했어요" }
            throw e
        }
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
    waitTimeSecond: Int = 5,
) {
    val waitTime = Duration.ofSeconds(waitTimeSecond.toLong())
    withContext(IO) {
        val wait = WebDriverWait(this@waitUntilElementRendered, waitTime)
        wait.until(ExpectedConditions.visibilityOfElementLocated(element))
    }
}

suspend fun RemoteWebDriver.isElementRendered(
    element: By,
    waitTimeSecond: Int = 5,
): Boolean {
    val waitTime = Duration.ofSeconds(waitTimeSecond.toLong())
    return withContext(IO) {
        try {
            val wait = WebDriverWait(this@isElementRendered, waitTime)
            wait.until(ExpectedConditions.visibilityOfElementLocated(element))
            true
        } catch (e: Exception) {
            false
        }
    }
}

suspend fun RemoteWebDriver.waitUntilElementRemoved(
    element: By,
    waitTimeSecond: Int = 5,
) {
    val waitTime = Duration.ofSeconds(waitTimeSecond.toLong())
    withContext(IO) {
        val wait = WebDriverWait(this@waitUntilElementRemoved, waitTime)
        wait.until { driver ->
            val elements = driver.findElements(element)
            elements.isEmpty() || elements.all { !it.isDisplayed }
        }
    }
}

suspend fun ChromeDriver.doubleClickToJavaScript(
    button: WebElement,
) {
    withContext(IO) {
        toJavascriptExecutor().executeScript(CLICK, button)
        toJavascriptExecutor().executeScript(CLICK, button)
    }
}

fun ChromeDriver.screeShot() {
    val screenshot = this as TakesScreenshot
    val srcFile = screenshot.getScreenshotAs(OutputType.FILE)
    val destFile = File("screenshots/${LocalDateTime.now()}.png")
    srcFile.copyTo(destFile)
    logger.error { "스크린샷 -> screenshots/${LocalDateTime.now()}.png" }
}

fun RemoteWebDriver.toJavascriptExecutor() =
    this as JavascriptExecutor