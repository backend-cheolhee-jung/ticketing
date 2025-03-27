package com.example.component

import com.example.component.Yes24Xpath.Date.DATE_OF_RESERVATION
import com.example.component.Yes24Xpath.Date.DETAIL_MODAL_DATE_OF_RESERVATION
import com.example.component.Yes24Xpath.Login
import com.example.component.Yes24Xpath.Ticketing.DETAIL_CONTAINER
import com.example.component.Yes24Xpath.Ticketing.DETAIL_PAGE
import com.example.component.Yes24Xpath.Ticketing.RESERVATION_BUTTON
import com.example.component.Yes24Xpath.Ticketing.SELECT_SEAT_BUTTON
import com.example.util.constant.JavaScriptEvent.REDIRECT
import com.example.util.extension.toJavascriptExecutor
import com.example.util.extension.toXpath
import com.example.util.extension.waitUntilElementRendered
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.RemoteWebDriver
import kotlin.time.Duration.Companion.seconds

class Yes24Browser {
    suspend fun login(
        chrome: RemoteWebDriver,
        id: String,
        password: String,
    ) {
        withContext(IO) {
            chrome.findElement(Login.ID).sendKeys(id)
            chrome.findElement(Login.PASSWORD).sendKeys(password)
            chrome.findElement(Login.LOGIN_BUTTON).click()
        }
    }

    suspend fun moveTicketingPage(
        chrome: ChromeDriver,
    ) {
        withContext(IO) {
            with(chrome) {
                toJavascriptExecutor().executeScript(REDIRECT, DETAIL_PAGE)
            }
        }
    }

    suspend fun search(
        chrome: ChromeDriver,
    ) {
        withContext(IO) {
            with(chrome) {
                findElement(DATE_OF_RESERVATION).click()
                findElement(RESERVATION_BUTTON).click()

                waitUntilElementRendered(DETAIL_CONTAINER, 5.seconds)
                findElement(DETAIL_MODAL_DATE_OF_RESERVATION).click()
                findElement(SELECT_SEAT_BUTTON).click()
            }
        }
    }

    suspend fun scrap(
        chrome: RemoteWebDriver,
    ) {
        TODO("여기서 부터는 실제로 크롤링 해야함")
    }
}

object Yes24Xpath {
    object Login {
        val ID = "//*[@id=\"SMemberID\"]".toXpath()
        val PASSWORD = "//*[@id=\"SMemberPassword\"]".toXpath()
        val LOGIN_BUTTON = "//*[@id=\"btnLogin\"]".toXpath()
    }

    object Ticketing {
        const val DETAIL_PAGE = "http://ticket.yes24.com/Perf/51026?Gcode=009_300"
        val RESERVATION_BUTTON = "//*[@id=\"mainForm\"]/div[10]/div/div[4]/a[4]".toXpath()

        val DETAIL_CONTAINER = "//div[@id='ContentsArea']".toXpath()
        val SELECT_SEAT_BUTTON = "//*[@id=\"btnSeatSelect\"]".toXpath()
    }

    object Date {
        // 이건 day6 날짜
        // const val DATE_OF_RESERVATION = "//td[.//span[contains(@class, 'ui-state-default') and text()='21']]"
        // val DETAIL_MODAL_DATE_OF_RESERVATION = "//td[@class='select']/a[@id='2024-12-21']".toXpath()

        // 이건 박진영 날짜 - 예시 데이터임
        val DATE_OF_RESERVATION = "//td[.//a[contains(@class, 'ui-state-default') and text()='28']]".toXpath()
        val DETAIL_MODAL_DATE_OF_RESERVATION = "//td[@class='select']/a[@id='2024-12-28']".toXpath()
    }
}