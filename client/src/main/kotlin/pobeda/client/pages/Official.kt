package pobeda.client.pages

import pobeda.client.stucture.PageState
import pobeda.client.stucture.StandardPageComponent
import pobeda.common.info.FileInfo
import pobeda.common.info.General
import react.dom.a
import styled.StyledDOMBuilder


class OfficialComponent : StandardPageComponent<PageState>() {
    override fun StyledDOMBuilder<*>.page() {
        a(href = FileInfo.officialPDF, target = "_blank") {
            +"Посмотреть или скачать «Положение о Всероссийской детско-юношеской акции \"${General.ruTitle} — 2021\"»"
        }
    }
}