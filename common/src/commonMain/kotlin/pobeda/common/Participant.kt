package pobeda.common

import kotlinx.serialization.Serializable

@Serializable
data class Participant(
    @ModelField("Уникальный ключ", autoIncremented = true, isPrimaryKey = true)
    val id: Int? = null,

    @ModelField("Время")
    val time: String,

    @ModelField("Фамилия")
    val surname: String,

    @ModelField("Имя")
    var name: String,

    @ModelField("Возраст")
    val age: Int,

    @ModelField("Населённый пункт")
    val city: String,

    @ModelField("Образовательное учреждение")
    val school: String,

    @ModelField("E-mail")
    val email: String,

    @ModelField("Название рисунка (без кавычек)")
    val title: String,

    @ModelField("Загрузите рисунок")
    val fileName: String,

    @ModelField("Старое название рисунка")
    val oldFileName: String,

    @ModelField("Есть куратор/преподаватель")
    val supervisor: Boolean,

    @ModelField("ФИО куратора/преподавателя", nullable = true)
    val supervisorFIO: String,

    @ModelField("E-mail или телефон куратора/преподавателя", nullable = true)
    val supervisorContacts: String,

    @ModelField("Добавить эссе")
    val essay: Boolean,

    @ModelField("Название эссе (по желанию)", nullable = true)
    val essayTitle: String,

    @ModelField("Загрузите эссе (по желанию)", nullable = true)
    val essayFileName: String,

    @ModelField("Старое название файла эссе", nullable = true)
    val essayOldFileName: String,

    @ModelField("Текст эссе (по желанию)", longText = true, nullable = true)
    val essayText: String
)