package pobeda.client.elements.input

import kotlinx.css.*
import kotlinx.css.properties.border
import kotlinx.html.DIV
import kotlinx.html.INPUT
import kotlinx.html.LABEL
import pobeda.client.elements.InputComponent
import pobeda.client.elements.InputItemState
import pobeda.client.gray50Color
import styled.StyledDOMBuilder
import styled.css

class SubmitInputComponent : InputComponent<InputItemState>() {
    override fun StyledDOMBuilder<INPUT>.inputBody() {
        css {
            height = 30.px
            marginBottom = 100.px //TODO marginBottom in all pages
            if (props.enable) backgroundColor = Color("#ddd")
            if (!props.enable) border(1.px, BorderStyle.solid, gray50Color)
            color = if (props.enable) Color.black else Color("#888")
            if (props.enable) cursor = Cursor.pointer
        }
    }

    override fun StyledDOMBuilder<LABEL>.labelBody() {

    }

    override fun StyledDOMBuilder<DIV>.containerBody() {
    }

}