package pobeda.server.generated

import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import pobeda.common.info.General
import pobeda.server.respondCss

fun Route.generateStylesCSS(path: String) {
    get(path) {
        call.respondCss {
            rule("@font-face") {
                fontFamily = "'pobeda'"
                put("src", "url('/fonts/pobeda.woff2') format('woff2'), " +
                    "url('/fonts/pobeda.woff') format('woff'), " +
                    "url('/fonts/pobeda.svg') format('svg')")
            }
            rule("*") {
                fontFamily = "'pobeda'"
                margin = "0"
                padding = "0"
                lineHeight = LineHeight("110%")
                letterSpacing = 90.pct
            }
            body {
                fontSize = General.defaultFontSize.px
            }
            rule(".wrapper") {
                width = 100.pct
                height = 100.pct
                display = Display.flex
                justifyContent = JustifyContent.center
            }
            rule(".wrapper-loading") {
                alignItems = Align.center
            }
            rule(".js-loading") {
                display = Display.flex
                color = Color("#008899")
            }
            rule("#js-response") {
                position = Position.absolute
                width = General.width.px
            }
        }
    }
}