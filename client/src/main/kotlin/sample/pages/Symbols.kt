package sample.pages

import kotlinx.css.*
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import react.setState
import sample.callAPI
import sample.info.FileInfo
import sample.json
import sample.stucture.StandardPageComponent
import sample.stucture.YamlListState
import sample.stucture.initYamlListState
import styled.StyledDOMBuilder
import styled.css
import styled.styledImg

class SymbolsComponent : StandardPageComponent<YamlListState<String>>() {
    init {
        initYamlListState()
        callAPI("get-yaml", "yaml=symbols") {
            setState {
                yaml.addAll(json.parse(String.serializer().list, responseText))
            }
        }
    }

    override fun StyledDOMBuilder<*>.page() {
        if (state.yaml != undefined) {
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
}