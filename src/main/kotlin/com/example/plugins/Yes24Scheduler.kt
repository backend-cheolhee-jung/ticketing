package com.example.plugins

import com.example.usecase.ScrapYes24UseCase
import extension.ktor.schedule
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.yes24Scheduler() {
    val scrapYes24UseCase by inject<ScrapYes24UseCase>()

    schedule("0 0 0 * * ?") {
        scrapYes24UseCase.execute()
    }
}