package pobeda.client.stucture

import kotlinx.css.JustifyContent
import kotlinx.css.justifyContent
import pobeda.client.MainStyles
import pobeda.client.RoutedProps
import pobeda.client.elementInBox
import pobeda.common.info.PageInfo
import react.RBuilder
import react.RComponent
import react.RState
import react.router.dom.routeLink
import styled.css
import styled.styledSpan

class LeftNavComponent : RComponent<RoutedProps, RState>() {
    private fun RBuilder.leftNavElement(to: String, text: String, x1: Int, y1: Int, x2: Int, y2: Int) {
        elementInBox(x1, y1, x2, y2) {
            css {
                justifyContent = JustifyContent.flexStart
            }
            styledSpan {
                css {
                    if (props.current == to) +MainStyles.current
                    +MainStyles.leftNavElement
                }
                routeLink(to) {
                    +text
                }
            }
        }
    }

    override fun RBuilder.render() {
        PageInfo.apply {
            leftNavElement(Join.url, Join.ruName.toUpperCase(), 420, 1170, 805, 1220)
            leftNavElement(About.url, About.ruName.toUpperCase(), 420, 1337, 805, 1387)
            leftNavElement(Official.url, Official.ruName.toUpperCase(), 420, 1502, 805, 1552)
            leftNavElement(News.url, News.ruName.toUpperCase(), 420, 1668, 805, 1718)
            leftNavElement(Team.url, Team.ruName.toUpperCase(), 395, 1835, 714, 1885)
            leftNavElement(Partners.url, Partners.ruName.toUpperCase(), 379, 1999, 679, 2049)
        }
    }

}