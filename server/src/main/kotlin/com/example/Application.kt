package com.example

import com.example.plugins.configureChrome
import com.example.plugins.configureDependencyInjection
import com.example.plugins.configureRouting
import com.example.plugins.configureStopApplication
import com.example.plugins.yes24Scheduler
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    yes24Scheduler()
    configureChrome()
    configureStopApplication()
    configureDependencyInjection()
    configureRouting()
}