package sample.stucture

import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.css.properties.borderTop
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import sample.elementInBox
import sample.info.General
import styled.css
import styled.styledSpan


class FooterComponent : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        elementInBox(228, 3264, 2132, 3490) {
            css {
                backgroundColor = Color.rosyBrown
                opacity = 0.5
            }
        }

        elementInBox(228, 3546, 2132, 3667) {
            styledSpan {
                css {
                    margin(2.px, 20.px)
                    padding(2.px, 0.px)
                    borderTop(2.px, BorderStyle.dashed, Color.black)
                    lineHeight = LineHeight("70%")
                }
                +General.ruTitle.toUpperCase()
                +". Полное или частичное копирование материалов сайта запрещено"
                +", при согласованном копировании ссылка на ресурс обязательна."
            }
        }
    }
}