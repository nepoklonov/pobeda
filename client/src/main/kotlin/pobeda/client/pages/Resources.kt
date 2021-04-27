package pobeda.client.pages

import kotlinx.css.*
import kotlinx.serialization.builtins.ListSerializer
import pobeda.client.gray50Color
import pobeda.client.send
import pobeda.client.stucture.StandardPageComponent
import pobeda.client.stucture.YamlListState
import pobeda.client.stucture.initYamlListState
import pobeda.client.stucture.updateYamlListState
import pobeda.common.Request
import pobeda.common.Resource
import pobeda.common.info.FileInfo
import pobeda.common.interpretation.YamlRef
import react.dom.a
import react.dom.p
import styled.*

class ResourcesComponent : StandardPageComponent<YamlListState<Resource>>() {
    init {
        initYamlListState()
        Request.GetYaml(YamlRef.ResourcesYaml).send(ListSerializer(Resource.serializer()), ::updateYamlListState)
    }

    override fun StyledDOMBuilder<*>.page() {
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