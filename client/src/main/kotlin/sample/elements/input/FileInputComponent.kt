package sample.elements.input

import kotlinx.css.*
import kotlinx.css.properties.border
import kotlinx.html.DIV
import kotlinx.html.INPUT
import kotlinx.html.LABEL
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.files.get
import org.w3c.xhr.FormData
import sample.callAPI
import sample.elements.InputComponent
import sample.randomString
import styled.StyledDOMBuilder
import styled.css

class FileInputComponent : InputComponent() {
    override fun StyledDOMBuilder<DIV>.containerBody() {}
    override fun StyledDOMBuilder<INPUT>.inputBody() {
        attrs.accept = if (props.name == "file")
            "image/*" else "text/plain,application/msword,application/vnd.oasis.opendocument.text"
        attrs.onChangeFunction = {
            val target = it.target as HTMLInputElement
            val formData = FormData()
            target.files?.get(0)?.let { file ->
                props.valueUpdate(props.name, file.name)
                formData.append(props.name, file, randomString(10))
                formData.append("original-name", file.name)
                formData.append("time", props.time)
                callAPI("load-file", formData) {
                    props.valueUpdate("old-" + props.name, file.name)
                    props.valueUpdate(props.name, responseText)
                }
            }
        }
        css {
            display = Display.none
        }
    }

    override fun StyledDOMBuilder<LABEL>.labelBody() {
        css {
            textAlign = TextAlign.center
            fontSize = 12.pt
            cursor = Cursor.pointer
            border(1.px, BorderStyle.solid, Color("#333"))
        }
    }
}
