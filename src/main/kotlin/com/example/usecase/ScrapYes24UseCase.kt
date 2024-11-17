package com.example.usecase

import com.example.component.Yes24Browser
import com.example.util.ChromeManager.newChrome
import com.example.util.constant.WebSite.Yes24
import com.example.util.extension.access
import com.example.util.extension.always
import com.example.util.extension.exit

class ScrapYes24UseCase(
    private val yes24Browser: Yes24Browser,
) {
    suspend fun execute() {
        val chrome = newChrome()

        runCatching {
            chrome.access(Yes24.URL)
            yes24Browser.login(chrome, Yes24.ID, Yes24.PASSWORD)
            yes24Browser.scrap(chrome)
        }.always {
            chrome.exit()
        }
    }
}