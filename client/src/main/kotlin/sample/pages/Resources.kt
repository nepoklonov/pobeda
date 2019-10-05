package sample.pages

import kotlinx.css.*
import kotlinx.serialization.list
import org.w3c.xhr.FormData
import react.dom.a
import react.dom.p
import react.setState
import sample.Resource
import sample.callAPI
import sample.gray50Color
import sample.info.FileInfo
import sample.json
import sample.stucture.StandardPageComponent
import sample.stucture.YamlListState
import sample.stucture.initYamlListState
import styled.*

class ResourcesComponent : StandardPageComponent<YamlListState<Resource>>() {
    init {
        initYamlListState()
        val formData = FormData()
        formData.append("yaml", "resources")
        callAPI("get-yaml", formData) {
            setState {
                yaml.addAll(json.parse(Resource.serializer().list, responseText))
            }
        }
    }

    override fun StyledDOMBuilder<*>.page() {
        if (state.yaml != undefined) {
            state.yaml.forEach {
                styledDiv {
                    css {
                        display = Display.flex
                        margin(25.px, 30.px, 25.px, 20.px)
                    }
                    a(href = it.link, target = "_blank") {
                        styledImg(src = FileInfo.Image.resourcesImagesDir + "/" + it.logo) {
                            css {
                                maxWidth = 100.px
                                maxHeight = 100.px
                            }
                        }
                    }
                    styledDiv {
                        css {
                            display = Display.flex
                            flexDirection = FlexDirection.column
                            justifyContent = JustifyContent.flexStart
                            paddingLeft = 10.px
                        }
                        p {
                            +it.name
                        }
                        styledP {
                            css {
                                marginTop = 5.px
                                color = gray50Color
                                fontSize = 10.pt
                            }
                            +it.text
                        }
                    }
                }
            }
        }
    }
}