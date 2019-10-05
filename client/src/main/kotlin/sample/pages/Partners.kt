package sample.pages

import kotlinx.css.maxHeight
import kotlinx.css.maxWidth
import kotlinx.css.px
import kotlinx.serialization.list
import org.w3c.xhr.FormData
import react.dom.a
import react.setState
import sample.Logos
import sample.callAPI
import sample.info.FileInfo
import sample.json
import sample.stucture.StandardPageComponent
import sample.stucture.YamlListState
import sample.stucture.initYamlListState
import styled.StyledDOMBuilder
import styled.css
import styled.styledImg

class PartnersComponent : StandardPageComponent<YamlListState<Logos>>() {
    init {
        initYamlListState()
        val formData = FormData()
        formData.append("yaml", "logos")
        callAPI("get-yaml", formData) {
            setState {
                yaml.addAll(json.parse(Logos.serializer().list, responseText))
            }
        }
    }
    override fun StyledDOMBuilder<*>.page() {
        if (state.yaml != undefined) {
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
}