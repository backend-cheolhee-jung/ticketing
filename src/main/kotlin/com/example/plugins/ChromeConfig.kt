package com.example.plugins

import com.example.plugins.ChromeManager.ChromeWebDriver
import io.ktor.server.application.*

fun Application.configureChrome() {
    // jar file run 할 때 env로 OS Type 넘겨서 드라이버 선택 실행하기. mac용, windows용 드라이버
    System.setProperty(ChromeWebDriver.ID, ChromeWebDriver.PATH)
}
