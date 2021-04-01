package pobeda.client.pages

import kotlinx.css.*
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import kotlinx.serialization.serializer
import org.w3c.dom.events.Event
import org.w3c.xhr.FormData
import pobeda.client.elements.input.CheckBoxInputComponent
import pobeda.client.elements.input.FileInputComponent
import pobeda.client.elements.input.SubmitInputComponent
import pobeda.client.elements.input.TextInputComponent
import pobeda.client.getISOSTime
import pobeda.client.gray70Color
import pobeda.client.send
import pobeda.client.stucture.PageState
import pobeda.client.stucture.StandardPageComponent
import pobeda.common.Participant
import pobeda.common.Request
import pobeda.common.associateBy
import pobeda.common.info.InputField
import react.dom.RDOMBuilder
import react.dom.a
import react.dom.br
import react.setState
import styled.StyledDOMBuilder
import styled.css
import styled.styledForm
import styled.styledP
import kotlin.browser.window

val types = mutableListOf("text", "checkbox", "file", "submit")
val inputClasses = mutableListOf(TextInputComponent::class,
    CheckBoxInputComponent::class, FileInputComponent::class, SubmitInputComponent::class) associateBy types
val inputTypes = mutableListOf(InputType.text, InputType.checkBox,
    InputType.file, InputType.submit) associateBy types

interface JoinState : PageState {
    var formSent: Boolean
    var enable: Boolean
}

class JoinComponent : StandardPageComponent<JoinState>() {

    private var time: String = getISOSTime()

    private val valueChanged: (String, String) -> Unit = { key, value ->
        setState {
            console.log(InputField.allFields[key]?.value, value, InputField.allFields.count {
                if (it.value.isValidIfExpected) {
                    false
                } else {
                    console.log(it.key, it.value)
                    true
                }
            })
            InputField.allFields[key]?.value = value
            val e = InputField.allFields.count { !it.value.isValidIfExpected } == 0
            if (enable != e) {
                enable = e
            }
        }
    }

    private val sendData: (Event) -> Unit = { e ->
        e.preventDefault()
        if (state.enable) {
            setState { enable = false }
            val formData = FormData()
            if (InputField.allFields.count {
                    if (it.value.willBeCollected) formData.append(it.key, it.value.value)
                    !it.value.isValidIfExpected
                } == 0) {
                InputField.run {
                    Request.FormSend(Participant(
                        null,
                        time.value,
                        surname.value,
                        name.value,
                        Regex("[^0-9]").replace(age.value, "").toInt(),
                        city.value,
                        school.value,
                        email.value,
                        title.value,
                        file.value,
                        oldFile.value,
                        supervisor.value == "yes",
                        supervisorFIO.value,
                        supervisorContacts.value,
                        essay.value == "yes",
                        essayTitle.value,
                        essayFile.value,
                        oldEssayFile.value,
                        essayText.value
                    )).send(String.serializer()) {
                        console.log(it)
                        if (it == "ok") {
                            setState {
                                allFields.forEach { it.value.value = if (it.value.type == "checkbox") "no" else "" }
                                formSent = true
                                window.scrollTo(0.0, 0.0)
                            }
                        }
                    }
                }
            } else {
                console.log("error is mistake")
                InputField.allFields.forEach {
                    console.log(it.key + " " + it.value.value)
                }
            }
        }
    }

    private fun initActions() {
        InputField.time.value = time
    }

    init {
        state.formSent = false
        state.enable = false
        initActions()
    }

    override fun StyledDOMBuilder<*>.page() {
        if (state.formSent) {
            styledP {
                css {
                    color = gray70Color
                }
                +"Спасибо за участие в акции, работа успешно загружена! "
//                +"В ближайшее время на указанную почту будет отправлен сертификат.
                br { }
                a(href = "") {
                    attrs.onClickFunction = { e ->
                        e.preventDefault()
                        setState {
                            formSent = false
                        }
                        initActions()
                    }
                    +"Загрузить ещё одну работу ->"
                }
            }
        } else {
            styledForm(action = "", method = FormMethod.post) {
                styledP {
                    css {
                        color = gray70Color
                    }
                    +"Чтобы принять участие в акции, заполните, пожалуйста, небольшую анкету:"
                }
                css {
                    margin(0.px, 30.px, 0.px, 30.px)
                    display = Display.flex
                    flexWrap = FlexWrap.wrap
                    justifyContent = JustifyContent.spaceBetween
                }
                attrs.onSubmitFunction = sendData
                InputField.allVisibleFields.forEach {
                    addInput(it.value)
                }
                styledP {
                    css {
                        color = gray70Color
                        marginBottom = 100.px
                    }
                    + "Пожалуйста, проверьте, что все поля заполнены. При возникновении проблем, обратитесь в службу поддержки сайта, указав возникшую проблему и приложив скрин экрана: uhatov007@yandex.ru"
                }
            }
        }
    }

    private fun RDOMBuilder<*>.addInput(inputField: InputField) = inputField.also { it ->
        inputClasses[it.type]?.let { inputClass ->
            child(inputClass) {
                attrs.also { a ->
                    a.type = inputTypes[it.type] ?: throw IllegalArgumentException()
                    a.time = time
                    a.name = it.name
                    a.title = it.title
                    a.isExpected = it.isExpected
                    a.validation = it.validation
                    a.valueUpdate = valueChanged
                    a.width = InputField.run {
                        when (it) {
                            surname -> 35.pct
                            name -> 30.pct
                            age -> 17.pct
                            title, essayTitle, submit -> 40.pct
                            file, essayFile -> 50.pct
                            else -> 100.pct
                        }
                    }
                    a.enable = state.enable
                }
            }
        }
    }
}
