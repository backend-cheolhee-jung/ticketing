package com.example.util.extension

fun <T, R> Iterable<T>.mapOrNull(
    transform: (T) -> R?,
): List<R?> {
    val destination = ArrayList<R?>()
    for (item in this) {
        val transformed = transform(item)
        destination.add(transformed)
    }
    return destination
}