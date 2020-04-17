package pobeda.client.elements.input

import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.INPUT
import kotlinx.html.LABEL
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import pobeda.client.elements.InputComponent
import pobeda.client.elements.InputItemState
import react.dom.a
import react.dom.br
import styled.StyledDOMBuilder
import styled.css

class CheckBoxInputComponent : InputComponent<InputItemState>() {
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
        when (props.title) {
            "$1" -> {
                +"Я даю согласие на обработку моих персональных данных."
                br {}
                a(target = "_blank", href = "/documents/fz.pdf") {
                    +"Ознакомиться с требованиями федерального закона о персональных данных"
                }
            }
            "$2" -> {
                +"Я ознакомлен с "
                a(target = "_blank", href = "documents/official.pdf") {
                    +"Положением об Акции"
                }
            }
            else -> {
                +props.title
            }
        }
    }
}
