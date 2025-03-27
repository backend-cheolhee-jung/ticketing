package com.example.util.extension

import org.openqa.selenium.By
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.toLocalDate(): LocalDate {
    val data = this.replace("-", "")
        .replace(",", "")
        .substring(0, 8)
    return LocalDate.parse(data, DateTimeFormatter.ofPattern("yyyyMMdd"))
}

fun String.toXpath(): By = By.xpath(this)

fun String.toCssSelector(): By = By.cssSelector(this)

fun String.removeAllWhitespace(): String = this.replace("\\s".toRegex(), "")

fun String.removeAllPrefix(
    prefixes: Array<String>,
): String {
    var result = this
    prefixes.forEach {
        result = result.replace(it, "")
    }
    return result
}