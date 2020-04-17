package pobeda.client.pages

import kotlinx.css.*
import kotlinx.serialization.list
import pobeda.client.gray70Color
import pobeda.client.send
import pobeda.client.stucture.StandardPageComponent
import pobeda.client.stucture.YamlListState
import pobeda.client.stucture.initYamlListState
import pobeda.client.stucture.updateYamlListState
import pobeda.common.Contact
import pobeda.common.Request
import pobeda.common.info.FileInfo
import pobeda.common.interpretation.YamlRef
import react.dom.a
import styled.*

class ContactsComponent : StandardPageComponent<YamlListState<Contact>>() {
    init {
        initYamlListState()
        Request.GetYaml(YamlRef.ContactsYaml).send(Contact.serializer().list, ::updateYamlListState)
    }

    override fun StyledDOMBuilder<*>.page() {
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