package pobeda.client.elements.input

import kotlinx.css.*
import kotlinx.css.properties.borderBottom
import kotlinx.html.DIV
import kotlinx.html.INPUT
import kotlinx.html.LABEL
import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import pobeda.client.elements.InputComponent
import pobeda.client.elements.InputItemState
import pobeda.client.gray50Color
import react.setState
import styled.StyledDOMBuilder
import styled.css

class TextInputComponent : InputComponent<InputItemState>() {
    override fun StyledDOMBuilder<DIV>.containerBody() {
        css {
            flexDirection = FlexDirection.columnReverse
            marginTop = if (!state.isEmpty) 15.px else 0.px
        }
    }

    override fun StyledDOMBuilder<INPUT>.inputBody() {
        attrs.onChangeFunction = {
            val target = it.target as HTMLInputElement
            props.valueUpdate(props.name, target.value)
            val wasCorrect = state.isCorrect || state.isIncorrect
            setState {
                isEmpty = target.value.isEmpty()
                isCorrect = props.validation(target.value)
                isIncorrect = !isCorrect && wasCorrect
            }
        }
        attrs.onBlurFunction = {
            setState {
                isIncorrect = !isCorrect
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
                backgroundColor = rgb(212, 235, 193)
                borderBottomColor = Color.limeGreen
            }
            if (state.isIncorrect) {
                if (!state.isEmpty) backgroundColor = Color.pink
                borderBottomColor = Color.darkRed
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
            if (!state.isEmpty && state.isCorrect) {
                backgroundColor = rgb(212, 235, 193)
                color = Color.darkSeaGreen
            }
            if (state.isIncorrect) {
                backgroundColor = Color.pink
                color = Color.darkRed
            }
        }
        +props.title
    }
}