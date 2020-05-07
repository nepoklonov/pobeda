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
        openFolders("images", "smi", "documents", "fonts", "yaml", "uploads", "js")

        static("/") {
            file("main.bundle.js")
        }

//        get("/tasks/05928/") {
//            val pText = File("yaml/p.json").readText()
//            val ivText = File("yaml/iv.json").readText()
//            val ps = Participant.serializer().list
//            val ivs = IV.serializer().list
//
//            val p = json.parse(ps, pText)
//            val iv = json.parse(ivs, ivText)
//
//            call.respondHtml {
//                +"yesssss"
//            }
//            transaction {
//                SchemaUtils.create(participantTable)
//                p.forEachIndexed { index, it ->
//                    println(index)
//                    participantTable.insert(it)
//                }
//                iv.forEachIndexed { index, i ->
//                    println(index)
//                    ImageVersions.insert {
//                        it[originalSrc] = i.originalSrc
//                        it[src] = i.src
//                        it[width] = i.width
//                        it[height] = i.height
//                        it[isOriginal] = i.isOriginal
//                    }
//                }
//            }
//            print("yessssss")
//        }

        generateLoadingHTML("{...}")
        generateStylesCSS("/styles.css")

        getYamlAPI()
        getImagesAPI()
        loadParticipantFileAPI()
        loadAdminParticipantFileAPI()
        loadFormAPI()
        getImageInfoAPI()
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
