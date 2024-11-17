package com.example.plugins

import com.example.component.Yes24Browser
import com.example.usecase.ScrapYes24UseCase
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(module)
    }
}

val module = module {
    single<ScrapYes24UseCase> { ScrapYes24UseCase(get()) }
    single<Yes24Browser> { Yes24Browser() }
}