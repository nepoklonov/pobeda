package pobeda.client.pages

import kotlinx.css.*
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import pobeda.client.send
import pobeda.client.stucture.StandardPageComponent
import pobeda.client.stucture.YamlListState
import pobeda.client.stucture.initYamlListState
import pobeda.client.stucture.updateYamlListState
import pobeda.common.Request
import pobeda.common.info.FileInfo
import pobeda.common.interpretation.YamlRef
import styled.StyledDOMBuilder
import styled.css
import styled.styledImg

class SymbolsComponent : StandardPageComponent<YamlListState<String>>() {
    init {
        initYamlListState()
        Request.GetYaml(YamlRef.SymbolsYaml).send(String.serializer().list, ::updateYamlListState)
    }

    override fun StyledDOMBuilder<*>.page() {
        css {
            display = Display.flex
            justifyContent = JustifyContent.spaceAround
            alignContent = Align.flexStart
            flexWrap = FlexWrap.wrap
        }

        for (i in 0 until state.yaml.size) {
            state.yaml[i].also {
                styledImg(src = FileInfo.Image.symbolsImagesDir + "/" + it) {
                    css {
                        width = if (i in arrayOf(0, 3)) 95.pct else 45.pct
                        margin(5.px, 0.px)
                    }
                }
            }
        }
    }
}