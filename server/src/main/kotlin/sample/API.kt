package sample

import com.charleskorn.kaml.Yaml
import io.ktor.application.call
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.request.receiveMultipart
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import sample.info.FileInfo
import sample.info.InputField
import java.io.File

suspend fun MultiPartData.parseForm(): MutableMap<String, String> {
    val map = mutableMapOf<String, String>()
    forEachPart { part ->
        if (part is PartData.FormItem) {
            map[part.name!!] = part.value
        }
        part.dispose()
    }
    return map
}

@Suppress("UNCHECKED_CAST")
fun Route.getYamlAPI(path: String) {
    post(path) {
        val post = call.receiveMultipart().parseForm()
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
}

fun Route.loadFileAPI(path: String) {
    post(path) {
        val post = mutableMapOf<String, String>()
        val multipart = call.receiveMultipart()
        val dir = "uploads"
        multipart.forEachPart { part ->
            if (part is PartData.FileItem) {
                val name = "!temp${part.originalFileName!!}!${randomString(7)}"
                post["hash-name"] = name
                val file = File("$dir/$name")
                part.streamProvider().use { its ->
                    file.outputStream().buffered().use { bufferedOutputStream ->
                        its.copyTo(bufferedOutputStream)
                    }
                }
            }
            if (part is PartData.FormItem) {
                post[part.name!!] = part.value
            }
            part.dispose()
        }
        val ext = File(post["original-name"]).extension
        val newPath = "$dir/${post["time"]}_${randomString(7)}.$ext"
        File("$dir/${post["hash-name"]}")
            .renameTo(File(newPath))
        call.respondText(newPath)
    }
}

fun Route.loadFormAPI(path: String) {
    post(path) {
        val post = call.receiveMultipart().parseForm()
        InputField.allFields.forEach { field ->
            field.value.value = post[field.key] ?: ""
        }
        if (InputField.allFields.count { field ->
                field.value.run { !isValid && willBeCollected }
            } == 0) {
            if (File(InputField.file.value).exists()) {
                call.respondText(addParticipant(InputField.allFields))
            } else {
                call.respondText("image file not uploaded")
            }
        } else {
            call.respondText("something is wrong")
        }
    }
}

fun Route.getImagesAPI(path: String) {
    post(path) {
        call.respondText(json.stringify(String.serializer().list, getAllImages()))
    }
}