package pobeda.server

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.http.ContentType
import io.ktor.http.content.file
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.routing
import kotlinx.css.CSSBuilder
import pobeda.server.generated.generateLoadingHTML
import pobeda.server.generated.generateStylesCSS

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
        openFolders("images", "smi", "documents", "fonts", "yaml", "uploads")

        static("/") {
            file("main.bundle.js")
        }

        generateLoadingHTML("{...}")
        generateStylesCSS("/styles.css")

        getYamlAPI()
        getImagesAPI()
        loadParticipantFileAPI()
        loadFormAPI()
        getImageInfoAPI()
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
