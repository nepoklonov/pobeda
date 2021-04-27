package pobeda.client

import org.w3c.files.Blob
import org.w3c.xhr.FormData
import org.w3c.xhr.XMLHttpRequest
import pobeda.common.Answer
import pobeda.common.Request
import pobeda.common.interpretation.Image
import pobeda.common.interpretation.ScaleContainer
import kotlinx.browser.window
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

inline fun callAPI(destination: String, params: FormData, crossinline action: XMLHttpRequest.() -> Unit) {
    val url = "${window.location.origin}/$destination"
    val xHTTP = XMLHttpRequest()
    xHTTP.open("POST", url)
    xHTTP.onreadystatechange = {
        if (xHTTP.readyState == 4.toShort()) {
            xHTTP.action()
        }
    }
    xHTTP.send(params)
}

fun String.toFormData(): FormData =
    FormData().apply {
        append("request", this@toFormData)
    }

class JSFile(val name: String, val value: Blob, val filename: String)

inline fun <reified R : Request> R.send(files: List<JSFile> = listOf(), crossinline action: (String) -> Unit) {
    callAPI(method.methodName, Json.encodeToString(this).toFormData().apply {
        files.forEach {
            append(it.name, it.value, it.filename)
        }
    }) {
        action(responseText)
    }
}

inline fun <reified R : Request, A> R.send(kSerializer: KSerializer<A>, files: List<JSFile> = listOf(), crossinline action: (A) -> Unit) = send(files) {
    val answer = Json.decodeFromString(Answer.serializer(kSerializer), it)
    action(answer.body)
}

inline fun <reified R : Request, A> R.send(kSerializer: KSerializer<A>, crossinline action: (A) -> Unit) = send(listOf()) {
    val answer = Json.decodeFromString(Answer.serializer(kSerializer), it)
    action(answer.body)
}

fun <T : ScaleContainer> Image<T>.getImageRealSrc(scale: T): String {
    return "NULL"
}

fun <T : ScaleContainer> Image<T>.getImageRealSrc(): String {
    return this.getImageRealSrc(this.scales[0])
}