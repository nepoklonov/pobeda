package pobeda.client.stucture

import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseOutFunction
import kotlinx.html.js.onMouseOverFunction
import org.w3c.dom.events.Event
import pobeda.client.elementInBox
import pobeda.client.scale
import pobeda.client.updateYaml
import react.RBuilder
import react.RComponent
import react.RProps
import react.setState
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv

interface GalleryPreviewState : YamlListState<String> {
    var i: Int
    var moused: Boolean
}

class GalleryPreviewComponent : RComponent<RProps, GalleryPreviewState>() {

    private val x1 = 911
    private val x2 = 2145
    private val y1 = 580
    private val y2 = 869

    private val horizontalAmount = 4
    private val zoom = 0.9

    private val dx = scale * (x2 - x1)
    private val dy = scale * (y2 - y1)

    private val width = (zoom * dx / horizontalAmount).toInt()
    private val height = (dy * zoom).toInt()

    init {
        state.i = 0
        initYamlListState()
        updateYaml(width, height)
    }

    private fun mouseIn(isMouseIn: Boolean): (Event) -> Unit = { _ -> setState { moused = isMouseIn } }

    private fun StyledDOMBuilder<*>.switchCss() = css {
        position = Position.absolute
        height = (zoom * 100).pct
        width = 30.px
        backgroundColor = Color("#66666688")
        hover {
            backgroundColor = Color("#666666bb")
        }
        active {
            backgroundColor = Color("#666666dd")
        }
        cursor = Cursor.pointer
        visibility = if (state.moused) Visibility.visible else Visibility.hidden
        zIndex = 1
        backgroundRepeat = BackgroundRepeat.noRepeat
        backgroundPosition = "50% 50%"
        active {
            backgroundPosition = "50% 47%"
        }
        backgroundSize = "40%"
    }

    override fun RBuilder.render() {
        elementInBox(x1, y1, x2, y2) {
            attrs.onMouseOverFunction = mouseIn(true)
            attrs.onMouseOutFunction = mouseIn(false)
            css {
                position = Position.absolute
                display = Display.flex
                justifyContent = JustifyContent.spaceBetween
            }
            styledDiv {
                switchCss()
                css {
                    left = 0.px
                    backgroundImage = Image("url('/images/design/go-left.png')")
                }
                attrs.onClickFunction = {
                    setState {
                        i--
                        if (i < 0) i += state.yaml.size
                    }
                }
            }
            styledDiv {
                switchCss()
                css {
                    right = 0.px
                    backgroundImage = Image("url('/images/design/go-right.png')")
                }
                attrs.onClickFunction = {
                    setState {
                        i++
                        if (i >= state.yaml.size) i -= state.yaml.size
                    }
                }
            }
            for (i in state.i until (state.i + horizontalAmount)) {
                val j = i % state.yaml.size
                styledDiv {
                    css {
                        opacity = 0.8
                        backgroundImage = Image("url('${state.yaml[j]}')")
                        backgroundRepeat = BackgroundRepeat.noRepeat
                        backgroundSize = "cover"
                        backgroundPosition = "center center"
                        width = this@GalleryPreviewComponent.width.px
                        height = this@GalleryPreviewComponent.height.px
                    }
                }
            }
        }
    }
}