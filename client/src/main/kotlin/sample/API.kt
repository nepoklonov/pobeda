package sample

import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.window

inline fun callAPI(destination: String, params: String, crossinline action: XMLHttpRequest.() -> Unit) {
    var url = "${window.location.origin}/api/$destination"
    var xhttp = XMLHttpRequest()
    xhttp.open("POST", url)
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded")
    xhttp.onreadystatechange = {
        if (xhttp.readyState == 4.toShort()) {
            xhttp.action()
        }
    }
    xhttp.send(params)
}