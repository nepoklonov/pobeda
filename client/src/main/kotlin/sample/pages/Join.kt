package sample.pages

import kotlinx.css.*
import kotlinx.css.properties.borderBottom
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.RDOMBuilder
import react.dom.form
import sample.gray50Color
import sample.stucture.PageState
import sample.stucture.StandardPageComponent
import styled.*
import kotlin.browser.window

interface JoinState : PageState {
//    var validMap: MutableMap<String, Boolean>
}

fun validateByPattern(sample: String, pattern: String) = Regex(pattern).matches(sample)

val validateEmail: (String) -> Boolean = {
    validateByPattern(it, "i.s.nepoklonov@yandex.ru")
}

val validateTypicalWords: (String) -> Boolean = {
    validateByPattern(it, "i.s.nepoklonov@yandex.ru")
}

val validateNumber: (String) -> Boolean = {
    validateByPattern(it, "i.s.nepoklonov@yandex.ru")
}

val validateCheckBox: (String) -> Boolean = {
    validateByPattern(it, "i.s.nepoklonov@yandex.ru")
}

interface InputItemProps : RProps {
    var type: InputType
    var name: String
    var title: String
    var validation: (String) -> Boolean
}

interface InputItemState : RState {
    var isEmpty: Boolean
    var isCorrect: Boolean
}

class InputItem : RComponent<InputItemProps, InputItemState>() {
    init {
        state.isEmpty = true
        state.isCorrect = false
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                display = Display.inlineFlex
                flexDirection = when (props.type) {
                    InputType.text -> FlexDirection.columnReverse
                    InputType.checkBox -> FlexDirection.row
                    else -> FlexDirection.column
                }
                margin(0.px, 10.px, 5.px, 10.px)
                if (state.isCorrect) {
                    backgroundColor = Color.green
                }
            }
            styledInput(type = props.type, name = props.name) {
                attrs {
                    onChangeFunction = {
                        val target = it.target as HTMLInputElement
                        setState {
                            isEmpty = target.value.isEmpty()
                            isCorrect = props.validation(target.value)
                        }
                    }
                }
                css {
                    if (props.type == InputType.text) {
                        position = Position.relative
                        border = "none"
                        backgroundColor = Color.transparent
                        borderBottom(1.px, BorderStyle.solid, gray50Color)
                        height = 20.px
                        zIndex = 2
                    } else if (props.type == InputType.checkBox) {
                        marginRight = 5.px
                    }
                    outline = Outline.none
                }
            }
            styledSpan {
                css {
                    if (props.type == InputType.text) {
                        position = Position.relative
                        if (state.isEmpty) {
                            top = 20.px
                            fontSize = 16.px
                        } else {
                            top = 2.px
                            fontSize = 12.px
                        }
                    }
                    color = gray50Color
                }
                +props.title
            }
        }
    }
}

fun RDOMBuilder<*>.inputItem(
    type: InputType,
    name: String,
    title: String,
    validation: (String) -> Boolean
) = child(InputItem::class) {
    attrs.also {
        it.type = type
        it.name = name
        it.title = title
        it.validation = validation
    }
}

class JoinComponent : StandardPageComponent<JoinState>() {
    override fun StyledDOMBuilder<*>.page() {
        form(method = FormMethod.post) {
            attrs.onSubmitFunction = {
                window.alert()
            }
            inputItem(InputType.text, "surname", "Фамилия", validateTypicalWords)
            inputItem(InputType.text, "name", "Имя", validateTypicalWords)
            inputItem(InputType.text, "age", "Возраст", validateNumber)
            inputItem(InputType.text, "city", "Населённый пункт", validateTypicalWords)
            inputItem(InputType.text, "school", "Образовательное учреждение", validateTypicalWords)
            inputItem(InputType.text, "email", "E-Mail", validateEmail)
            inputItem(InputType.checkBox, "supervisor", "Есть куратор/преподаватель", validateCheckBox)
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
