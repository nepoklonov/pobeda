package pobeda.client.stucture

import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.id
import pobeda.client.elementInBox
import react.*
import react.dom.h2
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv
import kotlin.browser.document

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

fun <T> RComponent<out RProps, out YamlListState<T>>.initYamlListState() {
    state.yaml = mutableListOf()
}

fun <T> RComponent<out RProps, out YamlListState<T>>.updateYamlListState(newYamlState: List<T>) {
    setState {
        yaml = newYamlState.toMutableList()
    }
}

fun <T> RComponent<out RProps, out YamlState<T>>.updateYamlState(newYamlState: T) {
    setState {
        yaml = newYamlState
    }
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
//    init {
//        val onscroll: (Event) -> Unit = onscroll@{
//            val pageBody = document.getElementById("page-body")
//            val root = document.getElementById("root")
//            if (pageBody == null || root == null) {
//                return@onscroll
//            }
//            val scrollY = max(window.scrollY - 360, 0.0)
//            pageBody.scrollTo(0.0, scrollY)
//            root.getBoundingClientRect().let { r ->
//                if (window.scrollY >= 360 && scrollY <
//                    pageBody.scrollHeight - pageBody.clientHeight) {
//                    props.scroll(true, r.left.px, r.top.px)
//                } else if (window.scrollY < 360) {
//                    props.scroll(false, 0.px, 0.px)
//                } else {
//                    props.scroll(false, 0.px,
//                        (pageBody.scrollHeight - pageBody.clientHeight).px)
//                }
//            }
//
//        }
//        window.onscroll = onscroll
//    }

    override fun componentDidMount() {
        val pageBody = document.getElementById("page-body")
        val root = document.getElementById("root")
        val rootParent = document.getElementById("root-parent")
        if (pageBody != null && root != null && rootParent != null) {
            val newHeight = pageBody.run { scrollHeight - clientHeight } + root.scrollHeight
            props.setHeight(newHeight.px)
            console.log("${pageBody.scrollHeight} ${pageBody.clientHeight} ${root.scrollHeight}")
        }
    }

    override fun componentDidUpdate(prevProps: PageProps, prevState: S, snapshot: Any) {
        componentDidMount()
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
                    //                    backgroundColor = Color("rgb(228, 234, 208)")
                    overflow = Overflow.auto
                    width = 100.pct
                    paddingRight = 18.px;
                    height = 100.pct
                }
                page()
            }
        }
    }
}