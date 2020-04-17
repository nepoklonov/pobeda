package pobeda.client.stucture

import kotlinx.css.*
import pobeda.common.info.FileInfo
import pobeda.common.info.General
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledImg

class BackgroundComponent : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        styledImg(src = FileInfo.Image.mainBackgroundImage.src) {
            css {
                width = General.width.px
                height = LinearDimension.auto
                position = Position.relative
                zIndex = -1
            }
        }
    }
}