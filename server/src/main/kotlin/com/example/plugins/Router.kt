package com.example.plugins

import com.example.usecase.ScrapYes24UseCase
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {
        val scrapYes24UseCase by inject<ScrapYes24UseCase>()

        get("/test") {
            scrapYes24UseCase.execute()
        }
    }
}