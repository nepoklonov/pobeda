package pobeda.client.stucture

import kotlinx.css.*
import pobeda.client.elementInBox
import pobeda.common.info.FileInfo
import pobeda.common.info.General
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.*

fun RBuilder.contactP(way: String, text: String) = styledP {
    css {
        display = Display.inline
    }
    styledImg (src= "${FileInfo.Image.contactsImagesDir}/$way.png") {
        css {
            width = 8.pt
            height = 8.pt
        }
    }
    styledSpan {
        css {
            fontSize = 12.px
        }
        +text
    }
}

class ContactsPreviewComponent : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        elementInBox(210, 2541, 828, 3044) {
            css {
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.flexStart
                alignItems = Align.flexStart
            }
            styledH3 {
                css {
                    margin = "0"
                    padding = "0"
                    width = 100.pct
                    textAlign = TextAlign.center
                }
                +"=Связь со штабом=".toUpperCase()
            }
            contactP("mail", General.eMail)
            contactP("vk", General.enName)
            contactP("fb", General.enName)
            contactP("inst", General.enName)
        }
    }
}