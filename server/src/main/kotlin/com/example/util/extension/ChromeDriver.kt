package com.example.util.extension

import com.example.plugins.ChromeManager
import com.example.util.constant.JavaScriptEvent.CLICK
import io.github.oshai.kotlinlogging.KotlinLogging
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

suspend fun ChromeDriver.doubleClickToJavaScript(
    button: WebElement,
) {
    withContext(IO) {
        toJavascriptExecutor().executeScript(CLICK, button)
        toJavascriptExecutor().executeScript(CLICK, button)
    }
}

fun RemoteWebDriver.toJavascriptExecutor() =
    this as JavascriptExecutor