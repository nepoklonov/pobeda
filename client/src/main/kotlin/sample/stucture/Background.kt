package sample.stucture

import kotlinx.css.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import sample.info.FileInfo
import sample.info.General
import styled.css
import styled.styledImg

class BackgroundComponent : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        styledImg(src = FileInfo.Image.mainBackgroundImage.src) {
            css {
                width = General.width.px
                height = LinearDimension.auto
                position = Position.absolute
                zIndex = -1
            }
        }
    }
}