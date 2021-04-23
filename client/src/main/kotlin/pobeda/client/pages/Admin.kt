package pobeda.client.pages

import kotlinx.css.*
import kotlinx.css.properties.border
import kotlinx.css.properties.borderRight
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onInputFunction
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import org.w3c.dom.HTMLInputElement
import pobeda.client.gray70Color
import pobeda.client.send
import pobeda.client.stucture.PageState
import pobeda.client.stucture.StandardPageComponent
import pobeda.common.Request
import pobeda.common.getPluralForm
import react.RBuilder
import react.dom.a
import react.dom.br
import react.setState
import styled.*

interface AdminState : PageState {
    var sent: Boolean
    var from: String
    var password: String
    var participants: List<List<List<String>>>
}

class AdminComponent : StandardPageComponent<AdminState>() {
    init {
        state.sent = false
        state.from = "0"
        state.password = ""
        state.participants = listOf()
    }

    override fun StyledDOMBuilder<*>.page() {

        styledSpan {
            +"Секретные слова: "
        }
        styledInput {
            css {
                width = 200.px
                height = 40.px
            }
            attrs.onInputFunction = {
                val v = (it.target as HTMLInputElement).value
                setState {
                    password = v
                }
            }
        }
        br {}

        styledSpan {
            +"необязательно | Начиная с: "
        }
        styledInput {
            css {
                width = 60.px
                height = 40.px
            }
            attrs.onInputFunction = {
                val v = (it.target as HTMLInputElement).value
                setState {
                    from = if (v == "") "0" else v
                }
            }
        }

        br {}
        styledButton {
            css {
                width = 80.px
                height = 40.px
            }
            +"Искать"
            attrs.onClickFunction = {
                setState {
                    sent = true
                }
                Request.ParticipantsGetAll(state.password, state.from.toInt()).send(ListSerializer(ListSerializer(ListSerializer(String.serializer())))) {
                    setState { participants = it }
                }
            }
        }
        if (state.sent) {
            fun RBuilder.line(key: String, vararg values: String?) {
                styledSpan {
                    css {
                        fontWeight = FontWeight.bold
                    }
                    +"$key: "
                }
                values.forEach { v ->
                    styledSpan {
                        css {
                            color = when (key) {
                                "id" -> Color.red
                                else -> Color.black
                            }
                            when (key) {
                                "id", "автор работы", "название" -> {
                                    backgroundColor = Color.white
                                }
                            }
                            borderRight(1.px, BorderStyle.solid, Color.pink)
                        }
                        +(v ?: "")
                    }
                }
                br { }
            }

            fun RBuilder.goNext() = styledDiv {
                css {
                    display = Display.flex
                    justifyContent = JustifyContent.spaceBetween
                }
                fun RBuilder.leftRight(vararg numbers: Int) = numbers.forEach { number ->
                    styledDiv {
                        a(href = "#") {
                            +if (number < 0) "$number <-" else "-> +$number"
                            attrs.onClickFunction = {
                                it.preventDefault()
                                val newFrom = (state.from.toInt() + number)
                                setState { from = newFrom.toString() }
                                setState { participants = listOf() }
                                Request.ParticipantsGetAll(state.password, newFrom).send(ListSerializer(ListSerializer(ListSerializer(String.serializer())))) {
                                    setState { participants = it }
                                }
                            }
                        }
                    }
                }


                styledDiv {
                    leftRight(-50, -200, -1000)
                }

                styledSpan {
                    +"${state.from} -- ${state.from.toInt() + 49}"
                }

                styledDiv {
                    leftRight(50, 200, 1000)
                }
            }

            goNext()

            state.participants.forEachIndexed { index, participant ->
                styledDiv {
                    css {
                        border(1.px, BorderStyle.solid, gray70Color)
                    }
                    participant.map { it[0] to it[1] }.toMap().let {
                        line("id", it["id"])
                        line("время загрузки", it["time"])
                        line("автор работы", it["surname"] + " ",
                            it["name"] + ", ",
                            it["age"] + " " + (it["age"]?.toIntOrNull()?.getPluralForm("год", "года", "лет"))
                                ?: "-")
                        line("название", it["title"])
                        it["fileName"]?.let { file -> "https://risuem-pobedu.ru/$file" }.let { file ->
                            styledA(target = "_blank", href = file) {
                                styledImg(src = it["mini"]?.let { ff -> "https://risuem-pobedu.ru/$ff" }) {
                                    css {
                                        height = 70.px
                                    }
                                }
                            }
                            br { }
                        }
                        it["fileName"]?.replace("uploads/(images|essays)/".toRegex(), "images/certs/cert-")
                            ?.replace("_[^-]*[.]".toRegex(), ".")?.replace("\\.\\w+$".toRegex(), ".jpg")?.let { cert ->
                                styledA(href = cert, target = "_blank") {
                                    css {
                                        color = Color.blueViolet
                                    }
                                    +"сертификат"
                                }
                            }
                        br {}
                        line("город", it["city"])
                        line("школа", it["school"])
                        line("email", it["email"])

                        val sup = listOf("supervisorFIO", "supervisorContacts")

                        if ((it["supervisor"] == "true") or sup.any { s -> it.containsKey(s) }) {
                            styledSpan {
                                css {
                                    fontWeight = FontWeight.bold
                                }
                                +"куратор: "
                            }
                            sup.filter { s -> it.containsKey(s) }.forEach { i ->
                                styledP {
                                    css {
                                        paddingLeft = 10.px
                                    }
                                    +(when (i) {
                                        "supervisorContacts" -> "контакты: "
                                        "supervisorFIO" -> "фио: "
                                        else -> " "
                                    } + it[i])
                                }
                            }
                        }

                        val es = listOf("essayTitle", "essayText", "essayFileName")

                        if ((it["essay"] == "true") or es.any { s -> it.containsKey(s) }) {
                            styledSpan {
                                css {
                                    fontWeight = FontWeight.bold
                                }
                                +"эссе: "
                            }
                            es.filter { s -> it.containsKey(s) }.forEach { i ->
                                styledP {
                                    css {
                                        paddingLeft = 10.px
                                    }
                                    when (i) {
                                        "essayTitle" -> {
                                            +"название эссе: "
                                            br {}
                                            styledP {
                                                css {
                                                    paddingLeft = 10.px
                                                }
                                                +(it[i] + "")
                                            }
                                        }
                                        "essayText" -> {
                                            +"текст эссе: "
                                            br {}
                                            styledP {
                                                css {
                                                    paddingLeft = 10.px
                                                }
                                                +(it[i] + "")
                                            }
                                        }
                                        "essayFileName" -> {
                                            it[i]?.let { f ->
                                                a(href = f, target = "_blank") {
                                                    +"файл эссе"
                                                }
                                            }
                                        }
                                        else -> {
                                            +""
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            goNext()
        }
    }
}