package sample.pages

import react.dom.a
import sample.info.FileInfo
import sample.info.General
import sample.stucture.PageState
import sample.stucture.StandardPageComponent
import styled.StyledDOMBuilder


class OfficialComponent : StandardPageComponent<PageState>() {
    override fun StyledDOMBuilder<*>.page() {
        a(href = FileInfo.officialPDF, target = "_blank") {
            +"Посмотреть или скачать «Положение о Всероссийской детско-юношеской акции \"${General.ruTitle} — 2019\"»"
        }
    }
}