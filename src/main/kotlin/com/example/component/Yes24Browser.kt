package com.example.component

import com.example.component.Yes24Xpath.Login
import com.example.util.extension.toXpath
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.openqa.selenium.remote.RemoteWebDriver

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

    suspend fun scrap(
        chrome: RemoteWebDriver,
    ) {
        TODO("여기서 부터는 실제로 크롤링 해야함")
    }
}

object Yes24Xpath {
    object Login {
        // TODO: 아래 path는 실제로 존재하지 않는 path입니다. 실제 path를 입력해주세요.
        val ID = "//*[@id='loginId']".toXpath()
        val PASSWORD = "//*[@id='loginPw']".toXpath()
        val LOGIN_BUTTON = "//*[@id='btnLogin']".toXpath()
    }
}