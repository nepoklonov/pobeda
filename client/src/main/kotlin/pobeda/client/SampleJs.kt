package pobeda.client

import kotlinx.css.*
import pobeda.client.pages.*
import pobeda.client.stucture.PageProps
import pobeda.client.stucture.RootComponent
import pobeda.common.info.General
import pobeda.common.info.PageInfo
import pobeda.pages.NewsComponent
import react.RComponent
import react.RElementBuilder
import react.RProps
import react.RState
import react.dom.render
import react.router.dom.browserRouter
import react.router.dom.route
import react.router.dom.switch
import styled.StyledComponents
import styled.injectGlobal
import kotlin.browser.document
import kotlin.js.Date
import kotlin.reflect.KClass

const val scale = General.scale

fun getISOSTime() = Date()
    .toISOString()
    .replace(':', '-')
    .replace('.', '-')
    .replace('T', '_')
    .replace("Z", "")

interface RoutedProps : RProps {
    var current: String
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

                    primitiveRoute(Admin, AdminComponent::class)

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