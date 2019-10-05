package sample.info

val alwaysGood: (String) -> Boolean = { true }
val filledCheckBox: (String) -> Boolean = { it == "yes" }
val anyCheckBox: (String) -> Boolean = { it == "yes" || it == "no" }

val validateByPattern: (String) -> (String) -> Boolean =
    { pattern -> { sample -> Regex(pattern, setOf(RegexOption.IGNORE_CASE)).matches(sample) } }

val validEmail = validateByPattern(".+@.+\\..+")
val validText = validateByPattern(".+")
val validNumber = validateByPattern("[1-9]\\d?")
val validImageFileName = validateByPattern(".+\\.(jpg|jpe|jpeg|tiff|webp|png|bmp|gif)")
val validEssayFileName = alwaysGood

data class InputField(
    val name: String,
    val title: String,
    val validation: (String) -> Boolean,
    val type: String,
    val owner: InputField? = null
) {
    var value: String = if (type == "checkbox") "no" else ""
    val isExpected: Boolean
        get() = owner == null || owner.value == "yes"
    val willBeCollected: Boolean
        get() = isExpected && this !in listOf(submit, agree, know)
    val isValid: Boolean
        get() = validation(value)
    val isValidIfExpected: Boolean
        get() = isValid || !isExpected

    init {
        if (owner != null && owner.type != "checkbox") {
            throw IllegalArgumentException("Wrong owner's type of $name")
        }
    }

    companion object {
        val surname = InputField("surname", "Фамилия", validText, "text")
        val name = InputField("name", "Имя", validText, "text")
        val age = InputField("age", "Возраст", validNumber, "text")
        val city = InputField("city", "Населённый пункт", validText, "text")
        val school = InputField("school", "Образовательное учреждение", validText, "text")
        val email = InputField("email", "E-Mail", validEmail, "text")
        val title = InputField("title", "Название рисунка", validText, "text")
        val file = InputField("file", "Загрузите рисунок", validImageFileName, "file")
        val supervisor = InputField("supervisor", "Есть куратор/преподаватель", anyCheckBox, "checkbox")
        val supervisorFIO = InputField("supervisorFIO", "ФИО куратора/преподавателя", validText, "text", owner = supervisor)
        val supervisorContacts = InputField("supervisorContacts", "E-mail или телефон куратора/преподавателя", validText, "text", owner = supervisor)
        val essay = InputField("essay", "Добавить эссе", anyCheckBox, "checkbox")
        val essayTitle = InputField("essayTitle", "Название эссе (по желанию)", alwaysGood, "text", owner = essay)
        val essayFile = InputField("essayFile", "Загрузите эссе (по желанию)", validEssayFileName, "file", owner = essay)
        val essayText = InputField("essayText", "Текст эссе (по желанию)", alwaysGood, "text", owner = essay)
        val agree = InputField("agree", "Я даю согласие на обработку моих персональных данных.\nОзнакомиться с требованиями федерального закона о персональных данных", filledCheckBox, "checkbox")
        val know = InputField("know", "Я ознакомлен с Положением об Акции", filledCheckBox, "checkbox")
        val submit = InputField("submit", "", alwaysGood, "submit")

        val time = InputField("time", "", validText, "text")
        val oldFile = InputField("old-file", "", validText, "text")
        val oldEssayFile = InputField("old-essayFile", "", alwaysGood, "text", owner = essay)

        //        val openFields = listOf(surname, name, age, city, title, file)
        val allVisibleFields = listOf(surname, name, age, city, school, email, title,
            file, supervisor, supervisorFIO, supervisorContacts, essay,
            essayTitle, essayFile, essayText, agree, know, submit).associateBy { it.name }
        val allFields = allVisibleFields + listOf(time, oldFile, oldEssayFile).associateBy { it.name }

    }
}
