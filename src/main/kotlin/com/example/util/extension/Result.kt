package com.example.util.extension

suspend fun <T> Result<T>.always(
    block: suspend () -> Unit
): T {
    block()
    return getOrThrow()
}