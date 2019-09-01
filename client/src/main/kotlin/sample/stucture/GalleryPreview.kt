package sample.stucture

import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.opacity
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import sample.elementInBox
import styled.css


class GalleryPreviewComponent : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        elementInBox(911, 580, 2145, 869) {
            css {
                backgroundColor = Color.rosyBrown
                opacity = 0.8
            }
        }
    }
}