package pobeda.client.pages

import kotlinx.css.*
import kotlinx.css.properties.borderBottom
import kotlinx.css.properties.borderTop
import pobeda.client.MainStyles
import pobeda.client.orangeBrightColor
import pobeda.client.scale
import pobeda.client.stucture.PageComponent
import pobeda.client.stucture.PageState
import pobeda.client.stucture.StandardPageComponent
import pobeda.common.info.FileInfo
import pobeda.common.info.General
import pobeda.common.quote
import react.dom.br
import react.dom.p
import styled.*
import kotlin.browser.document

class MainComponent : StandardPageComponent<PageState>() {
    init {
        document.title = "Рисуем Победу"
    }
    override fun StyledDOMBuilder<*>.page() {
        styledDiv {
            css {
                children("p") {
                    margin(5.px, 0.px)
                }
                padding(5.px)
                margin(5.px)
                backgroundColor = rgba(255, 255, 255, 0.2)
            }
            styledH3 {
                orangeSpan {
                    +"Рисуем и помним нашу Победу!"
                }
            }
            styledImg(src = "/images/design/post4.jpg") {
                css {
                    width = 100.pct
                }
            }
            styledP {
                +"С 23 февраля 2021 года стартует новый этап масштабной детско-юношеской патриотической акции «Рисуем Победу». Всероссийская акция, проводимая ежегодно, начиная с 2012 года, станет международной. Славные подвиги поколения победителей отразят в своих рисунках и эссе не только юные россияне, но и их сверстники из Белоруссии, Казахстана, Армении, Узбекистана, Киргизии и других государств."
            }
            styledP {
                +""
            }
            styledP {
                +"Впервые, Акция была проведена в Санкт-Петербурге. С каждым годом география и состав участников расширялись. В 2020 году, Акция объединила более 28000 участников из 2430 населённых пунктов Российской Федерации и 14 зарубежных государств."
            }
            styledP {
                +"Юным участникам Акции предлагается создать рисунок, а также, побеседовав со старшими написать небольшое эссе (по желанию), повествующее о жизни родных и близких в годы войны."
            }
            styledP {
                +"Цель Акции – сформировать и развить у детей и молодежи чувство патриотизма, уважения и сопричастности к славному ратному и гражданскому подвигу предков. В процессе творчества, на основе услышанного и осознанного исторического материала, участники вовлекаются в живой диалог поколений, узнают и осознают историю своей семьи и своей страны."
            }
            styledP {
                +"В целях безопасности и комфорта участников, создана специальная интернет-платформа http://risuem-pobedu.ru/. Для участия в Акции достаточно загрузить созданную творческую работу, заполнив краткую анкету."
            }
            styledP {
                +"По словам председателя оргкомитета Акции, депутата Государственной Думы Алёны Аршиновой, «такие творческие инициативы, направленные на сохранение памяти о Великой Отечественной войне, о мужестве Советского народа очень важны, а учитывая вызовы современной геополитики – жизненно необходимы»."
            }
            styledP {
                +"По мнению художественного руководителя акции, Владимира Головачева, «каждый рисунок несет в себе то уникальное видение, которым обладают только дети, с переживаниями и наблюдениями, связанными с «недетскими» понятиями: война, испытание, страх, подвиг, и, конечно – Победа»."
            }
            styledP {
                +"Жюри, в которое входят ветераны Великой Отечественной войны, офицеры армии и флота, известные художники и литераторы, ежегодно выбирает 50 финалистов, чьи работы представляются на передвижных выставках. Каждый участник Акции получает памятный электронный сертификат. Подведут итоги Акции 9 мая, в День Победы."
            }
            styledP {
                +""
            }
            styledP {
                +"Контактная информация:"
            }
            styledP {
                +"▪ Официальный сайт Акции: https://risuem_pobedy.ru/"
            }
            styledP {
                +"▪ E-mail: risyem-pobedy@yandex.ru"
            }
            styledP {
                +"▪ Официальные страницы Акции в социальных сетях: ВКонтакте: https://vk.com/risuem_pobedy; Инстаграм: https://www.instagram.com/risuem_pobedy/"
            }
            styledP {
                +"▪ Общая координация Акции: Головачев Владимир Сергеевич, e-mail: vladgolovachev@yandex.ru, м.т.: +7 (962) 706 77 40"
            }
        }
//        css {
//            alignItems = Align.flexStart
//        }
//        styledDiv {
//            css {
//                marginLeft = 10.px
//                width = (330 * scale).px
//            }
//            styledImg(src = FileInfo.Image.arshinovaMainImage.src) {
//                css {
//                    width = (330 * scale).px
//                    position = Position.relative
//                    top = (-10).px
//                }
//            }
//            styledImg(src = FileInfo.Image.defenderImage.src) {
//                css {
//                    width = (330 * scale).px
//                    marginTop = 20.pct
//                }
//            }
//        }
//        styledDiv {
//            css {
//                flexGrow = 0.0
//            }
//            styledDiv {
//                css {
//                    padding(5.px, 0.px)
//                    margin(40.px, 10.px)
//                    borderTop(2.px, BorderStyle.dashed, orangeBrightColor)
//                    borderBottom(2.px, BorderStyle.dashed, orangeBrightColor)
//                }
//                p {
////                    +"\u00abИз поколения в поколение мы передаём память о подвиге наших предков, храним дедовские медали, и о каждой из них рассказываем своим детям. Акция \u00abРисуем Победу\u00bb обращена к самым юным гражданам нашей страны – воспитанникам детских садов, школьникам. Ребята не только создают хуожественые произведеня, но и вместе с родителями вспоминают истоию своей семьи, своих дедов и прадедов\u00bb."
//                    +"«Из поколения в поколение мы передаём память о подвиге наших предков, храним дедовские медали, и о каждой из них рассказываем своим детям. Акция «Рисуем Победу» обращена к самым юным гражданам нашей страны – воспитанникам детских садов, школьникам. Ребята не только создают художественные произведения, но и вместе с родителями вспоминают историю своей семьи, своих дедов и прадедов»"
//                }
//                br {}
//                styledP {
//                    css {
//                        +MainStyles.orangeText
//                    }
//                    +"А. И. Аршинова"
//                }
//                p {
//                    +"Депутат Государственной Думы Российской Федерации, куратор Всероссийской акции ${General.ruTitle.quote()}"
//                }
//            }
//        }
//        styledImg(src = FileInfo.Image.mapWithBackImage.src) {
//            css {
//                position = Position.absolute
//                width = (1576 * scale).px
//                top = (1237 * scale).px
//                left = (-113 * scale).px
//            }
//        }
    }
}