package sample.pages

import kotlinx.css.*
import kotlinx.serialization.list
import org.w3c.xhr.FormData
import react.dom.a
import react.setState
import sample.Contacts
import sample.callAPI
import sample.gray70Color
import sample.info.FileInfo
import sample.json
import sample.stucture.StandardPageComponent
import sample.stucture.YamlListState
import sample.stucture.initYamlListState
import styled.*

class ContactsComponent : StandardPageComponent<YamlListState<Contacts>>() {
    init {
        initYamlListState()
        val formData = FormData()
        formData.append("yaml", "contacts")
        callAPI("get-yaml", formData) {
            setState {
                yaml.addAll(json.parse(Contacts.serializer().list, responseText))
            }
        }
    }
    override fun StyledDOMBuilder<*>.page() {
        if (state.yaml != undefined) {
            state.yaml.forEach {
                styledDiv {
                    css {
                        display = Display.flex
                        flexDirection = FlexDirection.row
                        alignItems = Align.center
                        margin(7.px, 30.px)
                    }
                    a(href = it.link, target = "_blank") {
                        styledImg(src = FileInfo.Image.contactsImagesDir + "/" + it.logo) {
                            css {
                                width = 20.px
                                height = 20.px
                            }
                        }
                    }
                    styledSpan {
                        css {
                            position = Position.relative
                            top = (-3).px
                            paddingLeft = 5.px
                            color = gray70Color
                        }
                        +it.text
                    }
                }
            }
        }
    }
}