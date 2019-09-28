package sample

import com.charleskorn.kaml.Yaml
import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import sample.info.FileInfo
import java.io.File

@Suppress("unchecked cast")
fun Route.getYamlAPI() {
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
}

fun Route.loadFileAPI() {
    post("api/load-file") {

    }
}

fun Route.loadFormAPI() {
    post("api/load-form") {

    }
}