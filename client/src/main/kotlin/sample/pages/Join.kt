package sample.pages

import kotlinx.html.FormMethod
import kotlinx.html.InputType
import react.RBuilder
import react.dom.RDOMBuilder
import react.dom.form
import react.dom.input
import react.dom.span
import sample.stucture.PageState
import sample.stucture.StandardPageComponent
import styled.StyledDOMBuilder

fun String.validateByPattern(pattern: String) = Regex(pattern).matches(this)

val validateEmail: String.() -> Boolean = {
    this.validateByPattern("i.s.nepoklonov@yandex.ru")
}

val validateTypicalWords: String.() -> Boolean = {
    this.validateByPattern("i.s.nepoklonov@yandex.ru")
}

val validateNumber: String.() -> Boolean = {
    this.validateByPattern("i.s.nepoklonov@yandex.ru")
}

val validateCheckBox: String.() -> Boolean = {
    this.validateByPattern("i.s.nepoklonov@yandex.ru")
}

inline fun RBuilder.inputItem(
        inputType: InputType,
        name: String,
        title: String,
        validation: String.() -> Boolean,
        block: RDOMBuilder<*>.() -> Unit = {}
) {
    span {
        +title
    }
    input(type = inputType, name = name) {
        block()
    }
}

interface JoinState : PageState {
    var validMap: MutableMap<Int, Boolean>
}

class JoinComponent : StandardPageComponent<JoinState>() {
    override fun StyledDOMBuilder<*>.page() {
        form(action = "send", method = FormMethod.post) {
            inputItem(InputType.text, "surname", "Фамилия", validateTypicalWords)
            inputItem(InputType.text, "name", "Имя", validateTypicalWords)
            inputItem(InputType.number, "age", "Возраст", validateNumber)
            inputItem(InputType.text, "city", "Населённый пункт", validateTypicalWords)
            inputItem(InputType.text, "school", "Образовательное учреждение", validateTypicalWords)
            inputItem(InputType.text, "email", "E-Mail", validateEmail)
            inputItem(InputType.checkBox, "supervisor", "Есть куратор/преподаватель", validateCheckBox) {
                //Add inputs enabling. How?

            }
            inputItem(InputType.text, "supervisorFIO", "ФИО куратора/преподавателя", validateTypicalWords)
            inputItem(InputType.text, "supervisorContacts", "E-mail или телефон куратора/преподавателя", validateTypicalWords)
            inputItem(InputType.text, "title", "Название рисунка", validateTypicalWords)
            inputItem(InputType.file, "file", "Загрузите рисунок", validateTypicalWords)
            inputItem(InputType.text, "essayTitle", "Название эссе (по желанию)", validateTypicalWords)
            inputItem(InputType.text, "essayText", "Текст эссе (по желанию)", validateTypicalWords)
            inputItem(InputType.file, "essayFile", "Загрузите рисунок", validateTypicalWords)
            inputItem(InputType.checkBox, "agree", "Я даю согласие на обработку моих персональных данных.\nОзнакомиться с требованиями федерального закона о персональных данных", validateCheckBox)
            inputItem(InputType.checkBox, "know", "Я ознакомлен с Положением об Акции", validateCheckBox)
            inputItem(InputType.submit, "submit", "Загрузить работу", validateCheckBox)
        }
    }
}
