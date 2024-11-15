package com.example.plugins

import com.example.util.ChromeManager
import io.ktor.server.application.*

fun Application.configureStopApplication() {
    ChromeManager.closeSessions()
}