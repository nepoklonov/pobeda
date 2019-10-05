package sample.elements

import kotlinx.css.*
import kotlinx.css.properties.borderBottom
import kotlinx.html.DIV
import kotlinx.html.INPUT
import kotlinx.html.LABEL
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import react.setState
import sample.gray50Color
import styled.StyledDOMBuilder
import styled.css

class TextInputComponent : InputComponent() {
    override fun StyledDOMBuilder<DIV>.containerBody() {
        css {
            flexDirection = FlexDirection.columnReverse
            if (!state.isEmpty) {
                marginTop = 15.px
            } else {
                marginTop = 0.px
            }
        }
    }

    override fun StyledDOMBuilder<INPUT>.inputBody() {
        attrs.onChangeFunction = {
            val target = it.target as HTMLInputElement
            props.valueUpdate(props.name, target.value)
            setState {
                isEmpty = target.value.isEmpty()
                isCorrect = props.validation(target.value)
            }
        }
        css {
            position = Position.relative
            border = "none"
            backgroundColor = Color.transparent
            borderBottom(1.px, BorderStyle.solid, gray50Color)
            height = 20.px
            zIndex = 2
            if (!state.isEmpty && state.isCorrect) {
                borderBottomColor = Color.green
            }
        }
    }

    override fun StyledDOMBuilder<LABEL>.labelBody() {
        css {
            if (state.isEmpty) {
                top = 20.px
                fontSize = 16.px
            } else {
                top = 2.px
                fontSize = 12.px
            }
        }
    }
}