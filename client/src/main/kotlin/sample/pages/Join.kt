package sample.pages

import kotlinx.css.*
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.js.onSubmitFunction
import react.setState
import sample.elements.inputItem
import sample.gray70Color
import sample.stucture.PageState
import sample.stucture.StandardPageComponent
import styled.StyledDOMBuilder
import styled.css
import styled.styledForm
import styled.styledP
import kotlin.browser.window

interface JoinState : PageState {
//    var validMap: MutableMap<String, Boolean>
var checkBoxMap: MutableMap<String, Boolean>
}

fun validateByPattern(sample: String, pattern: String) = Regex(pattern, setOf(RegexOption.IGNORE_CASE)).matches(sample)

val validateEmail: (String) -> Boolean = {
    validateByPattern(it, "(([^<>()[\\]\\.,;:\\s@\\\"]+(\\.[^<>()[\\]\\.,;:\\s@\\\"]+)*)|(\\\".+\\\"))@(([^<>()[\\]\\.,;:\\s@\\\"]+\\.)+[^<>()[\\]\\.,;:\\s@\\\"]{2,})")
}

val validateTypicalWords: (String) -> Boolean = {
    validateByPattern(it, "[^]+")
}

val validateNumber: (String) -> Boolean = {
    validateByPattern(it, "[1-9]\\d?")
}

val validateCheckBox: (String) -> Boolean = {
    false
}


class JoinComponent : StandardPageComponent<JoinState>() {
    init {
        state.checkBoxMap = mutableMapOf(Pair("essay", false), Pair("supervisor", false))
    }

    private val checkBoxUpdated: (s: String, b: Boolean) -> Unit = { s: String, b: Boolean ->
        setState {
            checkBoxMap[s] = b
        }
    }
    override fun StyledDOMBuilder<*>.page() {
        styledForm(action = "", method = FormMethod.post) {
            styledP {
                css {
                    color = gray70Color
                }
                +"Чтобы принять участие в акции, заполните, пожалуйста, небольшую анкету:"
            }
            css {
                margin(0.px, 30.px, 0.px, 30.px)
                display = Display.flex
                flexWrap = FlexWrap.wrap
                justifyContent = JustifyContent.spaceBetween
            }
            attrs.onSubmitFunction = {
                window.alert()
            }

            inputItem(InputType.text, "surname", "Фамилия", validation = validateTypicalWords, width = 35.pct)
            inputItem(InputType.text, "name", "Имя", validation = validateTypicalWords, width = 30.pct)
            inputItem(InputType.text, "age", "Возраст", validation = validateNumber, width = 17.pct)
            inputItem(InputType.text, "city", "Населённый пункт", validation = validateTypicalWords)
            inputItem(InputType.text, "school", "Образовательное учреждение", validation = validateTypicalWords)
            inputItem(InputType.text, "email", "E-Mail", validation = validateEmail)
            inputItem(InputType.text, "title", "Название рисунка", validation = validateTypicalWords, width = 40.pct)
            inputItem(InputType.file, "file", "Загрузите рисунок", validation = validateCheckBox, width = 40.pct)
            inputItem(InputType.checkBox, "supervisor", "Есть куратор/преподаватель", validation = validateCheckBox, checkBoxUpdated = checkBoxUpdated)
            if (state.checkBoxMap["supervisor"] == true) {
                inputItem(InputType.text, "supervisorFIO", "ФИО куратора/преподавателя", validation = validateTypicalWords)
                inputItem(InputType.text, "supervisorContacts", "E-mail или телефон куратора/преподавателя", validation = validateTypicalWords)
            }
            inputItem(InputType.checkBox, "essay", "Добавить эссе", validation = validateCheckBox, checkBoxUpdated = checkBoxUpdated)
            if (state.checkBoxMap["essay"] == true) {
                inputItem(InputType.text, "essayTitle", "Название эссе (по желанию)", validation = validateTypicalWords, width = 40.pct)
                inputItem(InputType.file, "essayFile", "Загрузите эссе (по желанию)", validation = validateCheckBox, width = 40.pct)
                inputItem(InputType.text, "essayText", "Текст эссе (по желанию)", validation = validateTypicalWords)
            }
            inputItem(InputType.checkBox, "agree", "Я даю согласие на обработку моих персональных данных.\nОзнакомиться с требованиями федерального закона о персональных данных", validation = validateCheckBox)
            inputItem(InputType.checkBox, "know", "Я ознакомлен с Положением об Акции", validation = validateCheckBox)
            inputItem(InputType.submit, "submit", " ", validation = validateCheckBox, width = 40.pct)
        }
    }
}
