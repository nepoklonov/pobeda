package sample

//import io.ktor.netty.Netty
import com.charleskorn.kaml.Yaml
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
import io.ktor.request.receiveParameters
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.css.*
import kotlinx.css.Display.flex
import kotlinx.css.Position.absolute
import kotlinx.css.properties.LineHeight
import kotlinx.html.*
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import sample.info.FileInfo
import sample.info.General
import java.io.File

fun Route.openFolder(folderName: String) {
    static("/$folderName") {
        files("$folderName")
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
//    embeddedServer(Jetty, 8080) {
    routing {
//        attributes.put(AttributeKey("Access-Control-Allow-Origin"), "*")
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

        get("/d") {
            val get = call.request.queryParameters
            call.respondText {
                get.toString()
            }
        }
        post("/api/get-yaml") {
            val post = call.receiveParameters()
            post["yaml"]?.let { yamlName ->
                if (yamlName in FileInfo.yamlList) {
                    val yamlText = File("${FileInfo.yamlDir}/$yamlName.yaml").readText()
                    val yamlSerializer = when (yamlName) {
                        "resources" -> Resource.serializer().list
                        "symbols" -> String.serializer().list
                        "team" -> Team.serializer()
                        "smi" -> Smi.serializer()
                        "contacts" -> Contacts.serializer().list
                        "logos" -> Logos.serializer().list
                        else -> throw IllegalArgumentException()
                    }
                    val yamlParsed = Yaml.default.parse(yamlSerializer, yamlText)
                    call.respondText(json.stringify(yamlSerializer as SerializationStrategy<Any>, yamlParsed))
                } else {
                    call.respondText("The yaml argument is wrong")
                }
            } ?: call.respondText("The yaml argument is empty")
        }
        post("api/load") {

        }
//    }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
