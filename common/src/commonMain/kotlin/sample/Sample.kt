package sample

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

fun Int.getPluralForm(one: String, two: String, five: String) = when {
    this in 10..20 -> five
    this % 10 == 1 -> one
    this % 10 in 2..4 -> two
    else -> five
}

fun String.quote() = "«" + this + "»"

val json = Json(JsonConfiguration.Stable)