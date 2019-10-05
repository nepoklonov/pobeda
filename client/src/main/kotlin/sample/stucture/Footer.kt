package sample.stucture

import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.css.properties.borderTop
import kotlinx.serialization.list
import org.w3c.xhr.FormData
import react.RBuilder
import react.RComponent
import react.RProps
import react.dom.a
import react.setState
import sample.Logos
import sample.callAPI
import sample.elementInBox
import sample.info.FileInfo
import sample.info.General
import sample.json
import styled.css
import styled.styledImg
import styled.styledSpan

class FooterComponent : RComponent<RProps, YamlListState<Logos>>() {
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
    override fun RBuilder.render() {
        elementInBox(228, 3264, 2132, 3490) {
            css {

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