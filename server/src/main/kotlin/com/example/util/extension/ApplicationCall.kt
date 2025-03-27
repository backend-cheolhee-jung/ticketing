package com.example.util.extension


import com.example.util.model.PathVariable
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.*
import io.ktor.server.application.*

val mapper = jacksonObjectMapper().apply {
    registerModule(JavaTimeModule())
    configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
}

val ApplicationCall.pathVariable
    get() = PathVariable(this)

inline fun <reified T : Any> ApplicationCall.getQueryParams(): T {
    return this.request.queryParameters.toClass()
}

inline fun <reified T : Any> Parameters.toClass(): T {
    val map = this.entries().associate { (key, value) ->
        if (value.size == 1) key to value.first()
        else key to value
    }

    return mapper.convertValue(map, T::class.java)
}