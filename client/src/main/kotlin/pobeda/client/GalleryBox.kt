package pobeda.client

import kotlinx.css.*
import kotlinx.html.classes
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import org.w3c.dom.Element
import pobeda.client.stucture.YamlListState
import pobeda.client.stucture.updateYamlListState
import pobeda.common.Request
import react.*
import react.dom.tbody
import styled.*

interface GalleryBoxProps : RProps {
    var getImages: (Int, Int) -> Unit
    var content: MutableList<String>
    var horizontalAmount: Int
    var proportion: Double
    var zoom: Double
}

interface GalleryBoxState : RState {
    var imageHeight: Int
    var bigSrc: String
}


val updateYaml: RComponent<out RProps, out YamlListState<String>>.(Int, Int) -> Unit = { width, height ->
    Request.ImagesGetAll(width, height).send(String.serializer().list) {
        updateYamlListState(it)
        js("blz()")
        Unit
    }
}

class GalleryBox : RComponent<GalleryBoxProps, GalleryBoxState>() {

    init {
        state.imageHeight = 0
        state.bigSrc = ""
    }

    private val tableRef = createRef<Element>()

    override fun componentDidUpdate(prevProps: GalleryBoxProps, prevState: GalleryBoxState, snapshot: Any) {
        tableRef.current?.getBoundingClientRect()?.width?.let {
            val h = (it / props.horizontalAmount * props.proportion).toInt()
            val w = (it / props.horizontalAmount).toInt()
            if (state.imageHeight != h) {
                setState {
                    imageHeight = h
                }
                props.getImages(w, h)
            }
        }
    }

    override fun RBuilder.render() {
        if (state.bigSrc != "") {
            child(BigImageComponent::class) {
                attrs {
                    src = state.bigSrc
                    close = {
                        setState {
                            bigSrc = ""
                        }
                    }
                }
            }
        }
        styledTable {
            ref = tableRef
            css {
                width = 100.pct
            }
            tbody {
                for (i in 0 until props.content.size + props.horizontalAmount step props.horizontalAmount) {
                    styledTr {
                        css.height = state.imageHeight.px
                        for (j in i until i + props.horizontalAmount) {
                            styledTd {
                                css {
                                    width = (100 / props.horizontalAmount).pct
                                    height = 100.pct
                                    display = Display.inlineFlex
                                    justifyContent = JustifyContent.center
                                    alignContent = Align.center
                                }
                                if (props.content.size > j) {
                                    styledDiv {
                                        attrs.onClickFunction = {
                                            setState {
                                                bigSrc = props.content[j]
                                                console.log(bigSrc)
                                            }
                                        }
                                        attrs.classes = setOf("b-lazy")
                                        attrs["data-src"] = props.content[j]
                                        css {
                                            cursor = Cursor.pointer
//                                            backgroundImage = Image("url('${props.content[j]}')")
                                            backgroundRepeat = BackgroundRepeat.noRepeat
                                            backgroundSize = "cover"
                                            backgroundPosition = "center center"
                                            width = (100 * props.zoom).pct
                                            height = (state.imageHeight * props.zoom).px
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}