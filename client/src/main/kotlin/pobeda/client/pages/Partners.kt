package pobeda.client.pages

import kotlinx.css.maxHeight
import kotlinx.css.maxWidth
import kotlinx.css.px
import kotlinx.serialization.builtins.ListSerializer
import pobeda.client.send
import pobeda.client.stucture.StandardPageComponent
import pobeda.client.stucture.YamlListState
import pobeda.client.stucture.initYamlListState
import pobeda.client.stucture.updateYamlListState
import pobeda.common.Logo
import pobeda.common.Request
import pobeda.common.info.FileInfo
import pobeda.common.interpretation.YamlRef
import react.dom.a
import styled.StyledDOMBuilder
import styled.css
import styled.styledImg

class PartnersComponent : StandardPageComponent<YamlListState<Logo>>() {
    init {
        initYamlListState()
        Request.GetYaml(YamlRef.LogosYaml).send(ListSerializer(Logo.serializer()), ::updateYamlListState)
    }

    override fun StyledDOMBuilder<*>.page() {
        state.yaml.forEach {
            a(href = it.link, target = "_blank") {
                styledImg(src = FileInfo.Image.partnersImagesDir + "/" + it.logo) {
                    css {
                        maxWidth = 200.px
                        maxHeight = 200.px
                    }
                }
            }
        }
    }
}