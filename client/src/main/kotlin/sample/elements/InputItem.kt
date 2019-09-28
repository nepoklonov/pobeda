package sample.elements

import kotlinx.css.*
import kotlinx.css.properties.border
import kotlinx.css.properties.borderBottom
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.files.get
import org.w3c.xhr.FormData
import react.*
import react.dom.RDOMBuilder
import sample.callAPI
import sample.gray50Color
import styled.css
import styled.styledDiv
import styled.styledInput
import styled.styledLabel

interface InputItemProps : RProps {
    var type: InputType
    var name: String
    var title: String
    var validation: (String) -> Boolean
    var width: LinearDimension
    var checkBoxUpdated: (String, Boolean) -> Unit
}

interface InputItemState : RState {
    var isEmpty: Boolean
    var isCorrect: Boolean
    var enabled: Boolean
}

class InputItem : RComponent<InputItemProps, InputItemState>() {
    init {
        state.isEmpty = true
        state.isCorrect = false
        state.enabled = true
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                display = Display.inlineFlex
                width = props.width
                flexDirection = when (props.type) {
                    InputType.text -> FlexDirection.columnReverse
                    InputType.checkBox -> FlexDirection.row
                    else -> FlexDirection.column
                }
                if (props.type == InputType.text) {
                    if (!state.isEmpty) {
                        marginTop = 15.px
                    }
                } else {
                    marginTop = 30.px
                }

            }
            styledInput(type = props.type, name = props.name) {
                attrs {
                    id = "input-" + props.name
                    onChangeFunction = {
                        val target = it.target as HTMLInputElement
                        setState {
                            isEmpty = target.value.isEmpty()
                            isCorrect = props.validation(target.value)
                        }
                        if (props.type == InputType.checkBox) {
                            props.checkBoxUpdated(props.name, target.checked)
                        } else if (props.type == InputType.file) {
                            val formData = FormData()
                            target.files?.get(0)?.let {
                                formData.append(props.name, it)
                                callAPI("load-file", formData) {
                                    console.log(this)
                                }
                            }
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
                    } else if (props.type == InputType.file) {
                        display = Display.none
                    }
                    outline = Outline.none
                }
            }
            styledLabel {
                attrs.htmlFor = "input-" + props.name
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
                    } else if (props.type == InputType.file) {
                        textAlign = TextAlign.center
                        fontSize = 12.pt
                        cursor = Cursor.pointer
                        border(1.px, BorderStyle.solid, Color("#333"))
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
    width: LinearDimension = 100.pct,
    checkBoxUpdated: (String, Boolean) -> Unit = { _: String, _: Boolean -> }
) = child(InputItem::class) {
    attrs.also {
        it.type = type
        it.name = name
        it.title = title
        it.validation = validation
        it.width = width
        it.checkBoxUpdated = checkBoxUpdated
    }
}
