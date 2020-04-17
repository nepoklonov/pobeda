package pobeda.common

val alwaysGood: (String) -> Boolean = { true }
val filledCheckBox: (String) -> Boolean = { it == "yes" }
val anyCheckBox: (String) -> Boolean = { it == "yes" || it == "no" }

fun getValidatorByPattern(r: Regex): (String) -> Boolean =
    Regex(r.pattern, RegexOption.IGNORE_CASE)::matches

val validEmail = getValidatorByPattern(".+@.+\\..+".toRegex())
val validText = getValidatorByPattern(".+".toRegex())
val validNumber = getValidatorByPattern("[1-9]\\d?".toRegex())
val validImageFileName = getValidatorByPattern(".+\\.(jpg|jpe|jpeg|tiff|webp|png|bmp|gif)".toRegex())
val validEssayFileName = alwaysGood