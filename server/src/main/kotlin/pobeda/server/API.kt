package pobeda.server

import com.charleskorn.kaml.Yaml
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.content.PartData
import io.ktor.http.content.readAllParts
import io.ktor.http.content.streamProvider
import io.ktor.request.receiveMultipart
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.util.pipeline.PipelineContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import pobeda.common.*
import pobeda.common.AnswerType.OK
import pobeda.common.AnswerType.WRONG
import pobeda.common.interpretation.getFileRefByName
import java.io.File

inline fun <reified T : Request> List<PartData>.receiveForm(): T {
    var result: String? = null
    forEach { part ->
        if (part is PartData.FormItem) {
            if (part.name!! == "request") result = part.value
            part.dispose()
        }
    }
    return result?.deserialize() ?: error("FUCK YOU")
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
    call.respondText(json.stringify(Answer.serializer(kSerializer), answer))
}

@Suppress("UNCHECKED_CAST")
fun Route.getYamlAPI() {
    post(Method.GetYaml.methodName) {
        try {
            val request = receiveOnlyForm<Request.GetYaml>()
            val yamlText = File(request.yamlRef.getFileRefByName().path).readText()
            val yamlSerializer = request.yamlRef.serializer
            val yamlParsed = Yaml.default.parse(yamlSerializer, yamlText)
            val answer = Answer(OK, yamlParsed)
            answer(answer, yamlSerializer as KSerializer<Any>)
        } catch (e: IllegalArgumentException) {
            answer(Answer(AnswerType.WRONG, "Wrong yaml name"), String.serializer())
        } catch (e: NoSuchElementException) {
            answer(Answer(AnswerType.WRONG, "Yaml name is missing"), String.serializer())
        }
    }
}

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
                if (fileData.fileType == "file") addImageVersions(newPath)
                answer(Answer(OK, newPath), String.serializer())
            } catch (e: NoSuchElementException) {
                answer(Answer(AnswerType.WRONG, "Necessary file info is missing"), String.serializer())
            }
        } else {
            answer(Answer(AnswerType.WRONG, "File is missing"), String.serializer())
        }
    }
}

fun Route.loadFormAPI() {
    post(Method.LoadForm.methodName) {
        val request = receiveOnlyForm<Request.FormSend>()
        val participant = request.participant

        if (File(participant.fileName).exists()) {
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
        } else {
            answer(Answer(WRONG, "image file not uploaded"), String.serializer())
        }

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
            answer(Answer(OK, getAllImages(request.width, request.height)), String.serializer().list)
        } catch (e: NoSuchElementException) {
            answer(Answer(AnswerType.WRONG, "You can request all images only put defined width and height"), String.serializer())
        }
    }
}

fun Route.getImageInfoAPI() {
    post(Method.GetImageInfo.methodName) {
        val request = receiveOnlyForm<Request.ImagesGetInfo>()
        try {
            answer(Answer(OK, getOpenParticipantInfo(request.src, request.width, request.height,
                request.all)), String.serializer().list)
        } catch (e: NoSuchElementException) {
            answer(Answer(AnswerType.WRONG, ":("), String.serializer())
        }
    }
}