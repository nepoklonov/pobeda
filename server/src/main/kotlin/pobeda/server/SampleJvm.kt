package pobeda.server

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.http.content.*
import io.ktor.response.respondText
import io.ktor.routing.*
import kotlinx.css.CSSBuilder
import pobeda.server.generated.generateLoadingHTML
import pobeda.server.generated.generateStylesCSS
import pobeda.server.openFolders

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

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.main(testing: Boolean = false) {
    install(CORS) {
        anyHost()
        allowCredentials = true
    }
    val client = io.ktor.client.HttpClient()
    routing {
        openFolders("images", "smi", "documents", "fonts", "yaml", "uploads", "js")

//        static("/") {
//            file("/resources/client.js")
//        }

        static("resources") {
            resources("/")
        }

        generateLoadingHTML("{...}")
        generateStylesCSS("/styles.css")

        initDB()

        getYamlAPI()
        getImagesAPI()
        loadParticipantFileAPI()
        yandexAllowAPI(client)
        loadAdminParticipantFileAPI()
        loadFormAPI()
        getImageInfoAPI()
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
