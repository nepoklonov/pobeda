package pobeda.server.generated

import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.html.*

fun Route.generateLoadingHTML(path: String) {
    get(path) {
        call.respondHtml {
            head {
                link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                link(rel = "icon", href = "/images/design/favicon.ico", type = "image/x-icon")
                meta(name = "yandex-verification", content = "bb87c21ee1d9d79a")
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
                script(src = "/js/blazy.min.js") {}
                script(src = "/js/b.js") {}
                script(src = "/main.bundle.js") {}
            }
        }
    }
}