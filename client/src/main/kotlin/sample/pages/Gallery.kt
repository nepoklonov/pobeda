package sample.pages

import kotlinx.css.maxHeight
import kotlinx.css.maxWidth
import kotlinx.css.px
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import org.w3c.xhr.FormData
import react.setState
import sample.callAPI
import sample.json
import sample.stucture.StandardPageComponent
import sample.stucture.YamlListState
import sample.stucture.initYamlListState
import styled.StyledDOMBuilder
import styled.css
import styled.styledImg


class GalleryComponent : StandardPageComponent<YamlListState<String>>() {
    init {
        initYamlListState()
        callAPI("get-images", FormData()) {
            setState {
                yaml.addAll(json.parse(String.serializer().list, responseText))
            }
        }
    }

    override fun StyledDOMBuilder<*>.page() {

        if (state.yaml != undefined) {
            state.yaml.forEach {
                styledImg(src = it) {
                    css {
                        maxWidth = 100.px
                        maxHeight = 100.px
                    }
                }
            }
        }
    }
}