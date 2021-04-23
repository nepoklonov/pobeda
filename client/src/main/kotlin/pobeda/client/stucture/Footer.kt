package pobeda.client.stucture

import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.css.properties.borderTop
import kotlinx.serialization.builtins.ListSerializer
import pobeda.client.elementInBox
import pobeda.client.send
import pobeda.common.Logo
import pobeda.common.Request
import pobeda.common.info.FileInfo
import pobeda.common.info.General
import pobeda.common.interpretation.YamlRef
import react.RBuilder
import react.RComponent
import react.RProps
import react.dom.a
import styled.css
import styled.styledImg
import styled.styledSpan

class FooterComponent : RComponent<RProps, YamlListState<Logo>>() {
    init {
        initYamlListState()
        Request.GetYaml(YamlRef.LogosYaml).send(ListSerializer(Logo.serializer()), ::updateYamlListState)
    }
    override fun RBuilder.render() {
        elementInBox(228, 3264, 2132, 3490) {
            css {
                display = Display.flex
                justifyContent = JustifyContent.spaceAround
            }
            if (state.yaml != undefined) {
                state.yaml.forEach {
                    a(href = it.link, target = "_blank") {
                        styledImg(src = FileInfo.Image.partnersImagesDir + "/" + it.logo) {
                            css {
                                maxWidth = 100.px
                                maxHeight = 80.px
                            }
                        }
                    }
                }
            }
        }

        elementInBox(228, 3546, 2132, 3667) {
            styledSpan {
                css {
                    margin(2.px, 20.px)
                    padding(2.px, 0.px)
                    borderTop(2.px, BorderStyle.dashed, Color.black)
                    lineHeight = LineHeight("70%")
                }
                +General.ruTitle.toUpperCase()
                +". Полное или частичное копирование материалов сайта запрещено"
                +", при согласованном копировании ссылка на ресурс обязательна."
            }
        }
    }
}