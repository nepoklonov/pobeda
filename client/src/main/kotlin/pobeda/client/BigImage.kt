package pobeda.client

import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import pobeda.client.stucture.YamlListState
import pobeda.client.stucture.initYamlListState
import pobeda.client.stucture.updateYamlListState
import pobeda.common.Request
import pobeda.common.getPluralForm
import react.RBuilder
import react.RComponent
import react.RProps
import react.dom.a
import react.dom.br
import styled.css
import styled.styledDiv
import styled.styledH2

interface BigImageProps : RProps {
    var src: String
    var close: () -> Unit
}

class BigImageComponent : RComponent<BigImageProps, YamlListState<String>>() {
    init {
        initYamlListState()
    }

    override fun RBuilder.render() {
        if (state.yaml.size == 0) {
            Request.ImagesGetInfo(props.src, 500, 500, true)
                .send(ListSerializer(String.serializer()), ::updateYamlListState)
        }
        styledDiv {
            css {
                display = Display.flex
                justifyContent = JustifyContent.center
                alignItems = Align.center
                position = Position.fixed
                top = 0.px
                left = 0.px
                width = 100.pct
                height = 100.pct
                zIndex = 2
            }
            styledDiv {
                attrs.onClickFunction = {
                    props.close()
                }
                css {
                    position = Position.absolute
                    width = 100.pct
                    height = 100.pct
                    backgroundColor = Color.black
                    opacity = 0.7
                }
            }
            if (state.yaml.size != 0) {
                styledDiv {
                    css {
                        width = 800.px
                        height = 500.px
                        backgroundColor = Color.white
                        zIndex = 3
                        display = Display.flex
                    }
                    styledDiv {
                        css {
                            backgroundImage = Image("url('${state.yaml[0]}')")
                            backgroundRepeat = BackgroundRepeat.noRepeat
                            backgroundSize = "cover"
                            backgroundPosition = "center center"
                            width = 500.px
                            height = 500.px
                        }
                    }
                    styledDiv {
                        css {
                            padding(20.px)
                            fontSize = 16.pt
                        }
                        styledH2 {
                            css.margin(10.px)
                            +state.yaml[5]
                        }
                        br { }
                        +state.yaml.let { "${it[1]} ${it[2]}," }
                        br {}
                        +state.yaml[3].let { "$it " + it.toInt().getPluralForm("год", "года", "лет") + "," }
                        br { }
                        +state.yaml[4]
                        br {}
                        a(target = "blank", href = state.yaml[6]) {
                            +"Открыть полную версию"
                        }
                    }
                }
            }
        }
    }
}
