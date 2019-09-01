package sample.pages

import kotlinx.css.maxHeight
import kotlinx.css.maxWidth
import kotlinx.css.px
import react.dom.a
import react.dom.p
import react.setState
import sample.Smi
import sample.callAPI
import sample.info.FileInfo
import sample.json
import sample.stucture.StandardPageComponent
import sample.stucture.YamlState
import styled.StyledDOMBuilder
import styled.css
import styled.styledImg

class SmiComponent : StandardPageComponent<YamlState<Smi>>() {
    init {
        state.yaml = Smi(mutableListOf(), mutableListOf())
        callAPI("get-yaml", "yaml=smi") {
            setState {
                val smiParsed = json.parse(Smi.serializer(), responseText)
                yaml.files.addAll(smiParsed.files)
                yaml.photos.addAll(smiParsed.photos)
            }
        }
    }

    override fun StyledDOMBuilder<*>.page() {
        if (state.yaml != undefined) {
            state.yaml.files.forEach {
                p {
                    a(href = it, target = "_blank") {
                        +it
                    }
                }
            }
            state.yaml.photos.forEach {
                styledImg(src = FileInfo.Image.smiImagesDir + "/$it") {
                    css {
                        maxWidth = 100.px
                        maxHeight = 100.px
                    }
                }
            }
        }
    }
}