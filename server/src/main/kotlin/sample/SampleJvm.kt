package sample

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.content.file
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.css.*
import kotlinx.css.Display.flex
import kotlinx.css.Position.absolute
import kotlinx.css.properties.LineHeight
import kotlinx.html.*
import sample.info.General

fun Route.openFolder(folderName: String) {
    static("/$folderName") {
        files(folderName)
    }
}

fun Route.openFolders(vararg folderNames: String) {
    for (folderName in folderNames) {
        openFolder(folderName)
    }
}

@Suppress("unused")
fun Application.main() {
    install(CORS) {
        anyHost()
        allowCredentials = true
    }
    routing {
        get("{...}") {
            call.respondHtml {
                head {
                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                }
                body("wrapper") {
                    div("wrapper") {
                        id = "js-response"
                        div("wrapper wrapper-loading") {
                            div("js-loading") {
                                +"Loading..."
                            }
                        }
                    }
                    script(src = "/main.bundle.js") {}
                }
            }
        }

        static("/") {
            file("main.bundle.js")
        }

        get("/styles.css") {
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
                    display = flex
                    justifyContent = JustifyContent.center
                }
                rule(".wrapper-loading") {
                    alignItems = Align.center
                }
                rule(".js-loading") {
                    display = flex
                    color = Color("#008899")
                }
                rule("#js-response") {
                    position = absolute
                    width = General.width.px
                }
            }
        }
        openFolders("images", "smi/documents", "fonts", "yaml")
        getYamlAPI()
        loadFileAPI()
        loadFormAPI()
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
