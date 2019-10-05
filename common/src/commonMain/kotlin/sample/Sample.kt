package sample

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.random.Random

fun Int.getPluralForm(one: String, two: String, five: String) = when {
    this in 10..20 -> five
    this % 10 == 1 -> one
    this % 10 in 2..4 -> two
    else -> five
}

fun String.quote() = "«" + this + "»"

fun randomString(amount: Int) = (('0'..'9').toList() + ('a'..'z').toList()).let {
    CharArray(amount) { _ -> it[Random.nextInt(0, it.size)] }
}.joinToString("")


val json = Json(JsonConfiguration.Stable)

infix fun <T, R> List<T>.associateWith(base: List<R>): Map<R, T> {
    val n = this.size
    if (n != base.size) throw IllegalArgumentException("Lists' size must be equal")
    val map = mutableMapOf<R, T>()
    for (i in 0 until n) {
        map[base[i]] = this[i]
    }
    return map
}