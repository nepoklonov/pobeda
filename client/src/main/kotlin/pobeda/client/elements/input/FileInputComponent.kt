package pobeda.client.elements.input

import kotlinx.css.*
import kotlinx.css.properties.border
import kotlinx.css.properties.deg
import kotlinx.css.properties.rotate
import kotlinx.css.properties.transform
import kotlinx.html.DIV
import kotlinx.html.INPUT
import kotlinx.html.LABEL
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseOutFunction
import kotlinx.html.js.onMouseOverFunction
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.get
import pobeda.client.JSFile
import pobeda.client.elements.InputComponent
import pobeda.client.elements.InputItemState
import pobeda.client.gray50Color
import pobeda.client.send
import pobeda.common.FileData
import pobeda.common.Request
import pobeda.common.info.InputField
import pobeda.common.randomString
import react.setState
import styled.StyledDOMBuilder
import styled.css
import styled.styledDiv

interface FileInputState : InputItemState {
    var moused: Boolean
    var miniFile: String
    var isLoading: Boolean
}

class FileInputComponent : InputComponent<FileInputState>() {
    init {
        state.moused = false
        state.miniFile = ""
        state.isLoading = false
    }

    private fun mouseIn(isMouseIn: Boolean): (Event) -> Unit = { _ -> setState { moused = isMouseIn } }

    override fun StyledDOMBuilder<DIV>.containerBody() {}
    override fun StyledDOMBuilder<INPUT>.inputBody() {
        attrs.accept = if (props.name == "file")
            "image/*" else ""//text/plain,application/msword,application/vnd.oasis.opendocument.text"
        attrs.onChangeFunction = {
            val target = it.target as HTMLInputElement
            target.files?.get(0)?.let { file ->
                props.valueUpdate(props.name, file.name)
                val jsFile = JSFile(props.name, file, randomString(10))
                val fileData = FileData(props.name, file.name, props.time)
                setState { isLoading = true }
                Request.FileUpload(fileData).send(String.serializer(), listOf(jsFile)) {
                    props.valueUpdate("old-" + props.name, file.name)
                    props.valueUpdate(props.name, it)
                    setState {
                        isEmpty = it.isEmpty()
                        moused = false
                    }
                    if (state.miniFile == "") {
//                        Request.ImagesGetInfo(InputField.file.value, 120, 80, false).send(ListSerializer(String.serializer())) {
                            setState {
                                isLoading = false
//                                miniFile = it[0]
                                miniFile = InputField.file.value
                            }
//                        }
                    }
                }
            }
        }
        css.display = Display.none
    }

    override fun StyledDOMBuilder<LABEL>.labelBody() {
        val type = state.isEmpty
        css {
            display = Display.flex
            alignItems = Align.center
            textAlign = TextAlign.center
            fontSize = 12.pt
            if (type) cursor = Cursor.pointer
            border(1.px, BorderStyle.solid, gray50Color)
            height = if (type) 50.px else 80.px
        }
        if (type) {
            attrs.onMouseOverFunction = mouseIn(true)
            attrs.onMouseOutFunction = mouseIn(false)
        }
        styledDiv {
            css {
                if (!type) {
                    attrs.onMouseOverFunction = mouseIn(true)
                    attrs.onMouseOutFunction = mouseIn(false)
                }
                cursor = Cursor.pointer
                width = 30.px
                height = 30.px
                margin(10.px, 15.px)
                display = Display.flex
                justifyContent = JustifyContent.center
                alignItems = Align.center
                backgroundRepeat = BackgroundRepeat.noRepeat
                backgroundSize = "60%"
                backgroundPosition = "center center"
                backgroundImage = Image("url('images/design/exit.png')")
                opacity = if (state.moused) 1 else 0.4
                transform { if (type) rotate(45.deg) }
            }
            attrs.onClickFunction = {
                if (!type) {
                    it.preventDefault()
                    setState {
                        isEmpty = true
                        miniFile = ""
                    }
                    props.valueUpdate(props.name, "")
                }
            }
        }
        styledDiv {
            if (props.name == "file" && InputField.file.isValid && state.miniFile.isNotEmpty()) {
                css {
                    height = 100.pct
                    backgroundImage = Image("url('${state.miniFile}')")
                    backgroundSize = "cover"
                    backgroundPosition = "center center"
                    flexGrow = 1.0
                }
            } else if (state.isLoading) {
                +"Идёт загрузка..."
            } else if (state.isEmpty) {
                +props.title
            } else {
                +"Файл загружен"
            }
        }

    }
}
