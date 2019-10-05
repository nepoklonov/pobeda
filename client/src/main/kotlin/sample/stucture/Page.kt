package sample.stucture

import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.id
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.h2
import sample.elementInBox
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv
import kotlin.browser.document
import kotlin.browser.window
import kotlin.math.max

interface PageProps : RProps {
    var pageName: String
    var ruPageName: String
    var scroll: (scrolling: Boolean, left: LinearDimension, top: LinearDimension) -> Unit
    var setHeight: (height: LinearDimension) -> Unit
}

interface PageState : RState {
//    var scroll: Double
}

interface YamlState<T> : PageState {
    var yaml: T
}

interface YamlListState<T> : YamlState<MutableList<T>>

fun <T> RComponent<out RProps, YamlListState<T>>.initYamlListState() {
    state.yaml = mutableListOf()
}

abstract class PageComponent<S : PageState> : RComponent<PageProps, S>() {
    inline fun RBuilder.pageDiv(content: StyledDOMBuilder<DIV>.() -> Unit) {
        elementInBox(995, 1173, 2165, 2964) {
            attrs.id = "page"
            content()
        }
    }

    override fun RBuilder.render() {
        pageDiv {
            page()
        }
    }

    abstract fun StyledDOMBuilder<*>.page()
}

abstract class StandardPageComponent<S : PageState> : PageComponent<S>() {
    init {
        val onscroll: (Event) -> Unit = onscroll@{
            val pageBody = document.getElementById("page-body")
            val root = document.getElementById("root")
            if (pageBody == null || root == null) {
                return@onscroll
            }
            val scrollY = max(window.scrollY - 360, 0.0)
            pageBody.scrollTo(0.0, scrollY)
            root.getBoundingClientRect().let { r ->
                if (window.scrollY >= 360 && scrollY <
                    pageBody.scrollHeight - pageBody.clientHeight) {
                    props.scroll(true, r.left.px, r.top.px)
                } else if (window.scrollY < 360) {
                    props.scroll(false, 0.px, 0.px)
                } else {
                    props.scroll(false, 0.px,
                        (pageBody.scrollHeight - pageBody.clientHeight).px)
                }
            }

        }
        window.onscroll = onscroll
    }

    override fun componentDidUpdate(prevProps: PageProps, prevState: S, snapshot: Any) {
        val pageBody = document.getElementById("page-body")
        val root = document.getElementById("root")
        val rootParent = document.getElementById("root-parent")
        if (pageBody != null && root != null && rootParent != null) {
            val newHeight = pageBody.run { scrollHeight - clientHeight } + root.scrollHeight
            props.setHeight(newHeight.px)
//            console.log("${pageBody.scrollHeight - pageBody.clientHeight + root.scrollHeight}")
        }
    }

    override fun RBuilder.render() {
        pageDiv {
            css {
                flexDirection = FlexDirection.column
                alignItems = Align.flexStart
                justifyContent = JustifyContent.flexStart
            }
            h2 {
                +props.ruPageName
            }
            styledDiv {
                attrs.id = "page-body"
                css {
                    backgroundColor = Color("rgb(228, 234, 208)")
                    overflow = Overflow.hidden
                    width = 100.pct
                    height = 100.pct
                }
                page()
            }
        }
    }
}