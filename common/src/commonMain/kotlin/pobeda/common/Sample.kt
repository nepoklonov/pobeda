package pobeda.common

import kotlinx.serialization.json.Json

fun Int.getPluralForm(one: String, two: String, five: String) = when {
    this in 10..20 -> five
    this % 10 == 1 -> one
    this % 10 in 2..4 -> two
    else -> five
}

fun String.quote() = "«$this»"

fun randomString(amount: Int) = (('0'..'9') + ('a'..'z')).let {
    CharArray(amount) { _ -> it.random() }
}.joinToString("")


val json = Json

infix fun <T, R> List<T>.associateBy(base: List<R>): Map<R, T> {
    require(size == base.size) { "Lists' size must be equal" }
    return (base.asSequence() zip asSequence()).toMap()
}

operator fun String.div(other: String): String {
    return "$this/$other"
}

operator fun String.minus(other: String): String {
    return "$this-$other"
}

infix fun String.usc(other: String): String {
    return "${this}_$other"
}

infix fun String.dot(other: String): String {
    return "$this.$other"
}