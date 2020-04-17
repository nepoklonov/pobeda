package pobeda.client.pages

import kotlinx.css.maxHeight
import kotlinx.css.maxWidth
import kotlinx.css.px
import pobeda.client.send
import pobeda.client.stucture.StandardPageComponent
import pobeda.client.stucture.YamlState
import pobeda.client.stucture.updateYamlState
import pobeda.common.Request
import pobeda.common.Smi
import pobeda.common.info.FileInfo
import pobeda.common.interpretation.YamlRef
import react.dom.a
import react.dom.p
import styled.StyledDOMBuilder
import styled.css
import styled.styledImg

class SmiComponent : StandardPageComponent<YamlState<Smi>>() {
    init {
        state.yaml = Smi(emptyList(), emptyList())
        Request.GetYaml(YamlRef.SmiYaml).send(Smi.serializer(), ::updateYamlState)
    }

    override fun StyledDOMBuilder<*>.page() {
        state.yaml.files.forEach {
            p {
                a(href = "/smi/documents/$it", target = "_blank") {
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