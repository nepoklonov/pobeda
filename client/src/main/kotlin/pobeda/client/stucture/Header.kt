package pobeda.client.stucture

import kotlinx.css.*
import kotlinx.css.properties.deg
import kotlinx.css.properties.rotate
import kotlinx.css.properties.transform
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import pobeda.client.*
import pobeda.common.Request
import pobeda.common.getPluralForm
import pobeda.common.info.FileInfo
import pobeda.common.info.General
import pobeda.common.info.PageInfo
import pobeda.common.quote
import react.RBuilder
import react.RComponent
import react.router.dom.navLink
import styled.css
import styled.styledSpan
import kotlin.js.Date

val months = arrayOf(
        "января",
        "февраля",
        "марта",
        "апреля",
        "мая",
        "июня",
        "июля",
        "августа",
        "сентября",
        "октября",
        "ноября",
        "декабря"
)

data class Today(val day: String, val month: String, val year: String)

fun today(): Today = Date().let {
    Today(it.getDate().toString(), months[it.getMonth()], it.getFullYear().toString())
}

val DAY_OF_THE_END = Date.UTC(2020, 4, 8).toLong()
const val msInDay = 1000 * 3600 * 24

fun getDaysLeft() = (-Date.now().toLong() / msInDay + DAY_OF_THE_END / msInDay).toInt()

class HeaderComponent : RComponent<RoutedProps, YamlListState<String>>() {
    private fun RBuilder.headerNavElement(to: String, text: String, userStyle: RuleSet = {}) = styledSpan {
        navLink(to) {
            +"| "
            +text
            +" |"
        }
        css {
            if (props.current == to) +MainStyles.current
            +MainStyles.hNavElement
            whiteSpace = WhiteSpace.nowrap
            userStyle()
        }
    }

    init {
        Request.ImagesGetAll(0, 0).send(String.serializer().list, ::updateYamlListState)
        initYamlListState()
    }

    override fun RBuilder.render() {
        today().also {
            labelInBox(it.day, 223, 210, 293, 280) {
                +MainStyles.orangeText
            }
            labelInBox(it.month, 360, 210, 560, 280)
            labelInBox(it.year, 629, 210, 759, 280)
        }
        getDaysLeft().also {
            labelInBox(it.toString(), 880, 210, 1000, 280) {
                +MainStyles.orangeText
            }
            labelInBox(it.getPluralForm("день", "дня", "дней") + " до финала", 1066, 210, 1480, 280)
        }
        (state.yaml.size).also {
            labelInBox(it.toString(), 1626, 210, 1839, 280) {
                +MainStyles.orangeText
            }
            labelInBox(it.getPluralForm("участник", "участника", "участников"), 1901, 210, 2155, 280)
        }
        navLink(to = PageInfo.Main.url) {
            imageInBox(FileInfo.Image.mainLogo.src, 240, 402, 743, 905) {
                width = 70.pct
                position = Position.relative
                top = 4.px
                left = 4.px
            }
        }
        labelInBox("Всероссийская акция ${General.ruTitle.quote()}".toUpperCase(), 888, 377, 2167, 432) {
            +MainStyles.orangeText
        }
        child(GalleryPreviewComponent::class) {}
        elementInBox(2282, 580, 2364, 873) {
            headerNavElement(PageInfo.Gallery.url, PageInfo.Gallery.ruName.toUpperCase()) {
                transform {
                    rotate(270.deg)
                }
            }
        }
        elementInBox(890, 1008, 2167, 1065) {
            css {
                justifyContent = JustifyContent.spaceAround
                put("word-spacing", "-5px")
            }
            PageInfo.apply {
                headerNavElement(Smi.url, Smi.ruName)
                headerNavElement(Symbols.url, Symbols.ruName)
                headerNavElement(Resources.url, Resources.ruName)
                headerNavElement(Contacts.url, Contacts.ruName)
            }
        }
    }
}