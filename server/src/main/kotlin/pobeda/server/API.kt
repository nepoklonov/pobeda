package pobeda.server

//import com.charleskorn.kaml.Yaml
import com.yandex.disk.rest.Credentials
import com.yandex.disk.rest.FileDownloadListener
import com.yandex.disk.rest.ResourcesArgs
import com.yandex.disk.rest.RestClient
import com.yandex.disk.rest.exceptions.http.HttpCodeException
import com.yandex.disk.rest.retrofit.CloudApi
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.html.currentTimeMillis
import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import pobeda.common.*
import pobeda.common.AnswerType.OK
import pobeda.common.AnswerType.WRONG
import pobeda.common.interpretation.PlanarSize
import pobeda.common.interpretation.getFileRefByName
import pobeda.common.interpretation.x
import java.io.File
import java.net.URLEncoder
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible


inline fun <reified T : Request> List<PartData>.receiveForm(): T {
    var result: String? = null
    forEach { part ->
        if (part is PartData.FormItem) {
            if (part.name!! == "request") result = part.value
            part.dispose()
        }
    }
    return Json.decodeFromString(result ?: error("FUCK YOU")) ?: error("FUCK YOU")
}


suspend inline fun <reified T : Request> PipelineContext<Unit, ApplicationCall>.receiveOnlyForm(): T =
    call.receiveMultipart().readAllParts().receiveForm()

fun List<PartData>.receiveFiles(dir: String = "uploads") =
    asSequence().filterIsInstance<PartData.FileItem>().map { part ->
        val file = createTempFile("!temp" - part.originalFileName!!, directory = File(dir))
        part.streamProvider().use { its ->
            file.outputStream().buffered().use { bufferedOutputStream ->
                its.copyTo(bufferedOutputStream)
            }
        }
        part.dispose()
        file.name
    }.toList()

suspend fun <T> PipelineContext<Unit, ApplicationCall>.answer(answer: Answer<T>, kSerializer: KSerializer<T>) {
    call.respondText(Json.encodeToString(Answer.serializer(kSerializer), answer))
}

@Suppress("UNCHECKED_CAST")
fun Route.getYamlAPI() {
    post(Method.GetYaml.methodName) {
        try {
            val request = receiveOnlyForm<Request.GetYaml>()
            val yamlText = File(request.yamlRef.getFileRefByName().path).readText()
            val yamlSerializer = request.yamlRef.serializer
            val yamlParsed = Json.decodeFromString(yamlSerializer as KSerializer<Any>, yamlText)
            val answer = Answer(OK, yamlParsed)
            answer(answer, yamlSerializer)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            answer(Answer(WRONG, "Wrong yaml name"), String.serializer())
        } catch (e: NoSuchElementException) {
            e.printStackTrace()
            answer(Answer(WRONG, "Yaml name is missing"), String.serializer())
        }
    }
}

fun RestClient.dirExists(path: String): Boolean {
    return try {
        getResources(ResourcesArgs.Builder().setPath(path).build())
        true
    } catch (e: HttpCodeException) {
        false
    }
}

fun RestClient.createDirs(path: String): Unit {
    if (dirExists(path)) return
    val prefix = path.substringBeforeLast('/')
    if (!dirExists(prefix)) {
        createDirs(prefix)
    }
    makeFolder(path)
}

data class FilePath(val storage: String, val url: String)

fun sendToYandex(file: File, path: String): FilePath {
    val yandex = RestClient(Credentials("polystorage", YandexCredentials.token))
    return try {
        yandex.createDirs("pobeda/2021/$path".substringBeforeLast('/'))
        val link = yandex.getUploadLink("pobeda/2021/$path", true).also(::println)
        yandex.uploadFile(link, true, file, null)
//        yandex.publish("pobeda/2021/$path")
//        println("---> plink $publishLink")
//        val pLinkTrue = yandex.getResources(ResourcesArgs.Builder().setPath("pobeda/2021/$path").build()).preview
//        println("---> plin2 $pLinkTrue")
        FilePath("yandex", "yandex?url=" + URLEncoder.encode("pobeda/2021/$path", "utf-8"))
    } catch (e: Exception) {
        e.printStackTrace()
        FilePath("server", path)
    }
}

//const val yandexPath = "https://disk.yandex.ru/d/WjNE2BzrDFYlXQ/"
//
//fun fromYandexOrServerToPath(storagePath: String, subdir: String = "images"): String {
//    return storagePath.substringAfter(yandexPath).let {
//        if (it == storagePath) it else "uploads/$subdir/$it"
//    }.also {
//        println("->eee $it")
//    }
//}
//
//fun fromCommonToStoragePath(commonPath: String, subdir: String = "images"): String {
//    return if (File(commonPath).exists()) commonPath else yandexPath + commonPath.substringAfter("uploads/$subdir/")
//}
//
//fun fromCommonToStoragePath(storage: String, commonPath: String, subdir: String = "images"): String {
//    return if (storage == "server") commonPath else yandexPath + commonPath.substringAfter("uploads/$subdir/")
//}

@Suppress("UNCHECKED_CAST", "FunctionName")
fun <T : Any, P : Any> T.`unsafe private`(name: String): P {
    val property = this::class.memberProperties.first {
        it.name == name
    }
    property as KProperty1<T, P>
    property.isAccessible = true
    return property.get(this)
}

fun Route.yandexAllowAPI(client: HttpClient, width: Int = 800, height: Int = 640) {
    get("yandex") {
        val path = call.request.queryParameters["url"]!!
        val file = File(path.substringAfter("pobeda/2021/")).also { println("loaded file: $it") }
        if (file.exists()) {
            println("exist!1")
            call.respondFile(file)
            return@get
        }
        println("file deleted: $file")
        val yandex = RestClient(Credentials("polystorage", YandexCredentials.token))
//        val resource = yandex.getResources(ResourcesArgs.Builder().setPath(path).build())
//        val url = resource.preview
//        yandex.downloadFile(call.request.queryParameters["url"]!!, null)
        val cloudApi: CloudApi = yandex.`unsafe private`("cloudApi")
        val url = cloudApi.getDownloadLink(path).href
//        val sizeStr = "&size=${width}x$height"
//        val previewUrl = url.replace("/disk/", "/preview/") + sizeStr

//        call.respondRedirect(previewUrl, false)
        val request = client.get<HttpResponse>(url) {
            headers {
                append(HttpHeaders.Authorization, "OAuth ${YandexCredentials.token}")
            }
        }

        call.respondBytesWriter(contentType = ContentType.defaultForFilePath(path)) {
            request.content.copyTo(this)
        }
//        println("--> ${request.request}\n\n ${request.request.headers}")
//        val bytes = request.content.toByteArray()
//        call.respondBytes(bytes)
    }
}


@OptIn(ExperimentalStdlibApi::class)
fun Route.loadParticipantFileAPI() {
    post(Method.FileUpload.methodName) {
        val allParts = call.receiveMultipart().readAllParts()
        val files = allParts.receiveFiles()
        if (files.isNotEmpty()) {
            val dir = "uploads"
            val request = allParts.receiveForm<Request.FileUpload>()
            try {
                val fileData = request.filesData[0]
                val subDir = if (fileData.fileType == "file") "images" else "essays"
                File(dir / subDir).let { d -> if (!d.exists()) d.mkdir() }
                val ext = File(fileData.originalFileName).extension
                val newPath = dir / subDir / fileData.namePrefix usc randomString(7) dot ext
                File(dir / files[0]).renameTo(File(newPath))
//                println("before: ${coroutineContext[CoroutineDispatcher]?.`unsafe private`<CoroutineDispatcher, Any>("delegate")}")
                GlobalScope.launch {
//                    println("inlaunch: ${coroutineContext[CoroutineDispatcher]?.`unsafe private`<CoroutineDispatcher, Any>("delegate")}")
                    if (fileData.fileType == "file") addImageVersions(newPath)
                }
//                println("haha after: ${coroutineContext[CoroutineDispatcher]?.`unsafe private`<CoroutineDispatcher, Any>("delegate")}")
                answer(Answer(OK, newPath), String.serializer())
            } catch (e: NoSuchElementException) {
                answer(Answer(WRONG, "Necessary file info is missing"), String.serializer())
            }
        } else {
            answer(Answer(WRONG, "File is missing"), String.serializer())
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun Route.loadAdminParticipantFileAPI() {
    post(Method.ParticipantsGetAll.methodName) {
        val request = call.receiveMultipart().readAllParts().receiveForm<Request.ParticipantsGetAll>()
        val password = request.password
        val from = request.from
        val size = 50
        val width = 200
        val height = 200
        if (password == AdminCredentials.secret) {
            println("correct admin pw")
            val t = database {
                participantTable.innerJoin(
                    ImageVersions,
                    { participantTable.getColumn("fileName") as Column<String> },
                    { src })
                    .slice(
                        participantTable.getColumn("id"),
                        ImageVersions.url,
                        ImageVersions.width,
                        ImageVersions.height,
                    ).select { ImageVersions.src eq participantTable.getColumn("fileName") }
                    .orderBy(participantTable.getColumn("id"), SortOrder.ASC)
                    .limit(size, from)
                    .groupBy(
                        keySelector = {
                            it[participantTable.getColumn("id") as Column<Int>]
                        },
                        valueTransform = {
                            it[ImageVersions.width] x it[ImageVersions.height] to it[ImageVersions.url]
                        }
                    ).map { (id, info) ->
                        ParticipantAdminDTO(
                            id,
                            chooseBestImageVersionSize(id, info, width, height)
                        )
                    }
            }
            answer(Answer(OK, t), ListSerializer(ParticipantAdminDTO.serializer()))
        }
    }
}


fun Route.loadFormAPI() {
    post(Method.LoadForm.methodName) {
        val request = receiveOnlyForm<Request.FormSend>()
        val participant = request.participant

//        if (!ImageVersions.select { ImageVersions.src eq participant.fileName }.empty()) {
        val okList = addParticipant(participant)
        answer(Answer(OK, okList[0]), String.serializer())
        if (okList[0] == "ok") {
            participant.run {
                val sFIO = if (supervisor) supervisorFIO else null
                var number = "20"
                repeat(5 - okList[1].length) { number += "0" }
                number += okList[1]
                sendCertificate(email, "$surname $name", time, number, sFIO)
            }
        }
//        } else {
//            answer(Answer(WRONG, "image file not uploaded"), String.serializer())
//        }

//        InputField.allFields.forEach { field ->
//            field.value.value = post[field.key] ?: ""
//        }
//        if (InputField.allFields.count { field ->
//                field.value.run { !isValid && willBeCollected }
//            } == 0) {
//           ...
//        } else {
//            call.respondText("something is wrong")
//        }
    }
}

fun Route.getImagesAPI() {
    post(Method.GetImages.methodName) {
        try {
            val request = receiveOnlyForm<Request.ImagesGetAll>()
            val amount = database {
                participantTable.selectAll().count()
            }
            answer(
                Answer(
                    OK,
                    Request.AllImages(getAllImages(request.width, request.height, request.page, request.size), amount)
                ),
                Request.AllImages.serializer()
            )
        } catch (e: NoSuchElementException) {
            e.printStackTrace()
            answer(
                Answer(WRONG, "You can request all images only put defined width and height"),
                String.serializer()
            )
        }
    }
}

fun Route.getImageInfoAPI() {
    post(Method.GetImageInfo.methodName) {
        val request = receiveOnlyForm<Request.ImagesGetInfo>()
        try {
            answer(
                Answer(
                    OK, getOpenParticipantInfo(
                        request.src, request.width, request.height,
                        request.all
                    )
                ), ListSerializer(String.serializer())
            )
        } catch (e: NoSuchElementException) {
            answer(Answer(WRONG, ":("), String.serializer())
        }
    }
}