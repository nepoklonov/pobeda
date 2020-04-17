package pobeda.client.pages

import kotlinx.css.*
import kotlinx.css.properties.borderBottom
import kotlinx.css.properties.borderTop
import pobeda.client.MainStyles
import pobeda.client.orangeBrightColor
import pobeda.client.scale
import pobeda.client.stucture.PageComponent
import pobeda.client.stucture.PageState
import pobeda.common.info.FileInfo
import pobeda.common.info.General
import pobeda.common.quote
import react.dom.br
import react.dom.p
import styled.*
import kotlin.browser.document

class MainComponent : PageComponent<PageState>() {
    init {
        document.title = "Рисуем Победу"
    }
    override fun StyledDOMBuilder<*>.page() {
        css {
            alignItems = Align.flexStart
        }
        styledDiv {
            css {
                marginLeft = 10.px
                width = (330 * scale).px
            }
            styledImg(src = FileInfo.Image.arshinovaMainImage.src) {
                css {
                    width = (330 * scale).px
                    position = Position.relative
                    top = (-10).px
                }
            }
            styledImg(src = FileInfo.Image.defenderImage.src) {
                css {
                    width = (330 * scale).px
                    marginTop = 20.pct
                }
            }
        }
        styledDiv {
            css {
                flexGrow = 0.0
            }
            styledDiv {
                css {
                    padding(5.px, 0.px)
                    margin(40.px, 10.px)
                    borderTop(2.px, BorderStyle.dashed, orangeBrightColor)
                    borderBottom(2.px, BorderStyle.dashed, orangeBrightColor)
                }
                p {
                    +"\u00abИз поколения в поколение мы передаём память о подвиге наших предков, храним дедовские медали, и о каждой из них рассказываем своим детям. Акция \u00abРисуем Победу\u00bb обращена к самым юным гражданам нашей страны – воспитанникам детских садов, школьникам. Ребята не только создают хуожественые произведеня, но и вместе с родителями вспоминают истоию своей семьи, своих дедов и прадедов\u00bb."

                }
                br {}
                styledP {
                    css {
                        +MainStyles.orangeText
                    }
                    +"А. И. Аршинова"
                }
                p {
                    +"Депутат Государственной Думы Российской Федерации, куратор Всероссийской акции ${General.ruTitle.quote()}"
                }
            }
        }
        styledImg(src = FileInfo.Image.mapWithBackImage.src) {
            css {
                position = Position.absolute
                width = (1576 * scale).px
                top = (1237 * scale).px
                left = (-113 * scale).px
            }
        }
    }
}