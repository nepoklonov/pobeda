package pobeda.client.stucture

import kotlinx.css.*
import kotlinx.html.id
import pobeda.client.RoutedProps
import react.RBuilder
import react.RComponent
import react.RState
import react.setState
import styled.css
import styled.styledDiv
import kotlin.reflect.KClass


interface RootProps : RoutedProps, PageProps {
    var pageComponent: KClass<out RComponent<PageProps, out RState>>
}

interface RootState : RState {
    var height: LinearDimension
    var scrolling: Boolean
    var fixedLeft: LinearDimension
    var fixedTop: LinearDimension
}

class RootComponent(props: RootProps) : RComponent<RootProps, RootState>(props) {
    init {
        state.height = 100.pct
        state.scrolling = false
        state.fixedLeft = 0.px
        state.fixedTop = 0.px
    }

    override fun RBuilder.render() {
        styledDiv {
            attrs.id = "root-parent"
            css {
                width = 100.pct
                height = state.height
            }
            styledDiv {
                attrs.id = "root"
                css {
                    width = 100.pct
                    position = Position.absolute
                    left = state.fixedLeft
                    top = state.fixedTop
                }
                child(BackgroundComponent::class) {}
                child(HeaderComponent::class) {
                    attrs.current = props.current
                }
                child(LeftNavComponent::class) {}
                child(ContactsPreviewComponent::class) {}
                child(props.pageComponent) {
                    attrs.pageName = props.pageName
                    attrs.ruPageName = props.ruPageName
                    attrs.scroll = { isScrolling, left, top ->
                        setState {
                            fixedLeft = left
                            fixedTop = top
                            scrolling = isScrolling
                        }
                    }
                    attrs.setHeight = { h ->
                        if (state.height.toString() != h.toString()) setState {
                            height = h
                        }
                    }
                }
                child(FooterComponent::class) {}
            }
        }
    }
}
