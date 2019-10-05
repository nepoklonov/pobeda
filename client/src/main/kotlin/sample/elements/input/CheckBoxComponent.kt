package sample.elements.input

import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.INPUT
import kotlinx.html.LABEL
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import sample.elements.InputComponent
import styled.StyledDOMBuilder
import styled.css

class CheckBoxInputComponent : InputComponent() {
    override fun StyledDOMBuilder<DIV>.containerBody() {
        css {
            flexDirection = FlexDirection.row
        }
    }

    override fun StyledDOMBuilder<INPUT>.inputBody() {
        attrs.onChangeFunction = {
            val target = it.target as HTMLInputElement
            props.valueUpdate(props.name, if (target.checked) "yes" else "no")
        }
        css {
            marginRight = 5.px
        }
    }

    override fun StyledDOMBuilder<LABEL>.labelBody() {
        css {
            top = (-3).px
        }
    }
}
