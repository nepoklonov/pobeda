package sample.pages

import kotlinx.css.*
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.events.Event
import org.w3c.xhr.FormData
import react.dom.RDOMBuilder
import react.dom.a
import react.dom.br
import react.setState
import sample.associateWith
import sample.callAPI
import sample.elements.TextInputComponent
import sample.elements.input.CheckBoxInputComponent
import sample.elements.input.FileInputComponent
import sample.elements.input.SubmitInputComponent
import sample.getISOSTime
import sample.gray70Color
import sample.info.InputField
import sample.stucture.PageState
import sample.stucture.StandardPageComponent
import styled.StyledDOMBuilder
import styled.css
import styled.styledForm
import styled.styledP
import kotlin.browser.window

val types = mutableListOf("text", "checkbox", "file", "submit")
val inputClasses = mutableListOf(TextInputComponent::class,
    CheckBoxInputComponent::class, FileInputComponent::class, SubmitInputComponent::class) associateWith types
val inputTypes = mutableListOf(InputType.text, InputType.checkBox,
    InputType.file, InputType.submit) associateWith types

interface JoinState : PageState {
    var formSent: Boolean
}

class JoinComponent : StandardPageComponent<JoinState>() {
    private var time: String = getISOSTime()

    private val valueChanged: (String, String) -> Unit = { key, value ->
        setState {
            InputField.allFields[key]?.value = value
        }
    }

    private val sendData: (Event) -> Unit = { e ->
        e.preventDefault()
        val formData = FormData()
        if (InputField.allFields.count {
                if (it.value.willBeCollected) formData.append(it.key, it.value.value)
                !it.value.isValidIfExpected
            } == 0) {
            callAPI("load-form", formData) {
                if (responseText == "ok") {
                    //TODO make certificate
                    setState {
                        formSent = true
                        window.scrollTo(0.0, 0.0)
                    }
                }
            }
        } else {
            console.log("error is mistake") //TODO make all red
        }
    }

    init {
        state.formSent = false
        InputField.time.value = time
    }

    override fun StyledDOMBuilder<*>.page() {
        if (state.formSent) {
            styledP {
                css {
                    color = gray70Color
                }
                +"Спасибо за участие в акции, работа успешно отправлена! "
                +"В ближайшее время на почту будет отправлен сертификат"
                br { }
                a(href = "") {
                    attrs.onClickFunction = { e ->
                        e.preventDefault()
                        setState {
                            formSent = false
                        }
                    }
                    +"Загрузить ещё одну анкету ->"
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
                            title, file, essayTitle, essayFile, submit -> 40.pct
                            else -> 100.pct
                        }
                    }
                }
            }
        }
    }
}
