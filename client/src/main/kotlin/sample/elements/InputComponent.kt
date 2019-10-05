package sample.elements

import kotlinx.css.*
import kotlinx.html.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import sample.gray50Color
import styled.*

interface InputItemProps : RProps {
    var type: InputType
    var name: String
    var title: String
    var width: LinearDimension
    var isExpected: Boolean
    var time: String
    var validation: (String) -> Boolean
    var valueUpdate: (String, String) -> Unit
}

interface InputItemState : RState {
    var isEmpty: Boolean
    var isCorrect: Boolean
}

abstract class InputComponent : RComponent<InputItemProps, InputItemState>() {

    abstract fun StyledDOMBuilder<DIV>.containerBody()
    abstract fun StyledDOMBuilder<INPUT>.inputBody()
    abstract fun StyledDOMBuilder<LABEL>.labelBody()

    init {
        state.isEmpty = true
        state.isCorrect = false
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                display = if (props.isExpected) Display.inlineFlex else Display.none
                width = props.width
                flexDirection = FlexDirection.column
                marginTop = 30.px
            }
            containerBody()
            styledInput(type = props.type, name = props.name) {
                attrs {
                    autoComplete = false
                    id = "input-" + props.name
                }
                css {
                    outline = Outline.none
                }
                inputBody()
            }
            styledLabel {
                attrs["htmlFor"] = "input-" + props.name
                css {
                    position = Position.relative
                    color = gray50Color
                }
                +props.title
                labelBody()
            }
        }
    }
}