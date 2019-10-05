package sample

import org.w3c.xhr.FormData
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.window

inline fun callAPI(destination: String, params: FormData, crossinline action: XMLHttpRequest.() -> Unit) {
    val url = "${window.location.origin}/api/$destination"
    val xHTTP = XMLHttpRequest()
    xHTTP.open("POST", url)
    xHTTP.onreadystatechange = {
        if (xHTTP.readyState == 4.toShort()) {
            xHTTP.action()
        }
    }
    xHTTP.send(params)
}
