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
import sample.gray50Color
import sample.gray70Color
import sample.stucture.PageState
import sample.stucture.StandardPageComponent
import styled.*
import kotlin.browser.window

interface JoinState : PageState {
//    var validMap: MutableMap<String, Boolean>
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

interface InputItemProps : RProps {
    var type: InputType
    var name: String
    var title: String
    var validation: (String) -> Boolean
    var width: LinearDimension
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
                width = props.width
                if (props.type == InputType.text) {
//                    minHeight = 45.px
                    flexDirection = FlexDirection.columnReverse
                    if (!state.isEmpty) {
                        marginTop = 15.px
                    }
                } else if (props.type == InputType.checkBox) {
                    marginTop = 30.px
                    flexDirection = FlexDirection.row
                } else {
                    marginTop = 30.px
                    flexDirection = FlexDirection.column
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
                        if (!state.isEmpty && state.isCorrect) {
                            borderBottomColor = Color.green
                        }
                    } else if (props.type == InputType.checkBox) {
                        marginRight = 5.px
                    }
                    outline = Outline.none
                }
            }
            styledSpan {
                css {
                    position = Position.relative
                    if (props.type == InputType.text) {
                        if (state.isEmpty) {
                            top = 20.px
                            fontSize = 16.px
                        } else {
                            top = 2.px
                            fontSize = 12.px
                        }
                    } else if (props.type == InputType.checkBox) {
                        top = (-3).px
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
    validation: (String) -> Boolean = { false },
    width: LinearDimension = 100.pct
) = child(InputItem::class) {
    attrs.also {
        it.type = type
        it.name = name
        it.title = title
        it.validation = validation
        it.width = width
    }
}

class JoinComponent : StandardPageComponent<JoinState>() {
    override fun StyledDOMBuilder<*>.page() {
        styledForm (action = "", method = FormMethod.post) {
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
            inputItem(InputType.text, "name", "Имя", validation = validateTypicalWords, width = 25.pct)
            inputItem(InputType.text, "age", "Возраст", validation = validateNumber, width = 17.pct)
            inputItem(InputType.text, "city", "Населённый пункт", validation = validateTypicalWords)
            inputItem(InputType.text, "school", "Образовательное учреждение", validation = validateTypicalWords)
            inputItem(InputType.text, "email", "E-Mail", validation = validateEmail)
            inputItem(InputType.checkBox, "supervisor", "Есть куратор/преподаватель", validation = validateCheckBox)
            inputItem(InputType.text, "supervisorFIO", "ФИО куратора/преподавателя", validation = validateTypicalWords)
            inputItem(InputType.text, "supervisorContacts", "E-mail или телефон куратора/преподавателя", validation = validateTypicalWords)
            inputItem(InputType.text, "title", "Название рисунка", validation = validateTypicalWords, width = 40.pct)
            inputItem(InputType.file, "file", "Загрузите рисунок", validation = validateCheckBox, width = 40.pct)
            inputItem(InputType.text, "essayTitle", "Название эссе (по желанию)", validation = validateTypicalWords, width = 40.pct)
            inputItem(InputType.text, "essayText", "Текст эссе (по желанию)", validation = validateTypicalWords)
            inputItem(InputType.file, "essayFile", "Загрузите рисунок", validation = validateCheckBox)
            inputItem(InputType.checkBox, "agree", "Я даю согласие на обработку моих персональных данных.\nОзнакомиться с требованиями федерального закона о персональных данных", validation = validateCheckBox)
            inputItem(InputType.checkBox, "know", "Я ознакомлен с Положением об Акции", validation = validateCheckBox)
            inputItem(InputType.submit, "submit", " ", validation = validateCheckBox, width = 40.pct)
        }
    }
}
