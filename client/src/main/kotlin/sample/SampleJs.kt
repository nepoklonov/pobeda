package sample

import kotlinx.css.*
import kotlinx.html.id
import react.*
import react.dom.render
import react.router.dom.browserRouter
import react.router.dom.route
import react.router.dom.switch
import sample.info.General
import sample.info.PageInfo
import sample.pages.*
import sample.stucture.*
import styled.StyledComponents
import styled.css
import styled.injectGlobal
import styled.styledDiv
import kotlin.browser.document
import kotlin.reflect.KClass

const val scale = General.scale

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
                display = Display.flex
                width = 100.pct
                height = state.height
            }
            styledDiv {
                attrs.id = "root"
                css {
                    width = 100.pct
                    height = 100.pct
                    position = if (state.scrolling) {
                        Position.fixed
                    } else {
                        Position.absolute
                    }
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
                    attrs.setHeight = {h ->
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

interface RoutedProps : RProps {
    var current: String
}

interface PageProps : RProps {
    var pageName: String
    var ruPageName: String
    var scroll: (scrolling: Boolean, left: LinearDimension, top: LinearDimension) -> Unit
    var setHeight: (height: LinearDimension) -> Unit
}

interface RootProps : RoutedProps, PageProps {
    var pageComponent: KClass<out RComponent<PageProps, out RState>>
}

interface RootState : RState {
    var height: LinearDimension
    var scrolling: Boolean
    var fixedLeft: LinearDimension
    var fixedTop: LinearDimension
}

fun RElementBuilder<RProps>.primitiveRoute(
    page: PageInfo,
    pageComponent: KClass<out RComponent<PageProps, out RState>>
) {
    route(page.url, exact = true) {
        child(RootComponent::class) {
            attrs.current = page.url
            attrs.pageComponent = pageComponent
            attrs.pageName = page.name
            attrs.ruPageName = page.ruName
        }
    }
}

fun main() {
    StyledComponents.injectGlobal {
        h2 {
            fontSize = 30.px
            margin(20.px, 0.px, 20.px, 30.px)
        }
        h3 {
            fontSize = 20.px
        }
    }
    render(document.getElementById("js-response")) {
        browserRouter {
            switch {
                PageInfo.run {
                    primitiveRoute(Main, MainComponent::class)
                    primitiveRoute(Smi, SmiComponent::class)
                    primitiveRoute(Symbols, SymbolsComponent::class)
                    primitiveRoute(Resources, ResourcesComponent::class)
                    primitiveRoute(Contacts, ContactsComponent::class)

                    primitiveRoute(Gallery, GalleryComponent::class)

                    primitiveRoute(Join, JoinComponent::class)
                    primitiveRoute(About, AboutComponent::class)
                    primitiveRoute(Official, OfficialComponent::class)
                    primitiveRoute(News, NewsComponent::class)
                    primitiveRoute(Team, TeamComponent::class)
                    primitiveRoute(Partners, PartnersComponent::class)
                }
            }
        }
    }
}