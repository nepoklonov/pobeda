package pobeda.client.pages

import kotlinx.css.*
import kotlinx.html.P
import kotlinx.html.SPAN
import pobeda.client.MainStyles
import pobeda.client.stucture.PageState
import pobeda.client.stucture.StandardPageComponent
import pobeda.common.info.FileInfo
import pobeda.common.info.General
import react.RBuilder
import react.dom.a
import react.dom.br
import react.dom.h3
import react.router.dom.navLink
import styled.*

fun RBuilder.orangeSpan(block: StyledDOMBuilder<SPAN>.() -> Unit) = styledSpan {
    css {
        +MainStyles.orangeText
    }
    block()
}

fun RBuilder.indentedP(block: StyledDOMBuilder<P>.() -> Unit) = styledP {
    css {
        +MainStyles.indented
    }
    block()
}

class AboutComponent : StandardPageComponent<PageState>() {
    override fun StyledDOMBuilder<*>.page() {
        styledDiv {
            css {
                padding(0.px, 30.px)
                child("p") {
                    margin(10.px, 0.px)
                }
                child("h3") {
                    marginTop = 20.px
                    textAlign = TextAlign.center
                }
            }
            styledA(href = FileInfo.officialPDF) {
                css {
                    marginBottom = 20.px
                }
                +"Скачать Положение"
            }
            indentedP {
                orangeSpan { +"Акция" }
                +" проводится ежегодно с 2012 года и охватывает все регионы России. Юным участникам предлагается создать "
                orangeSpan { +"рисунок" }
                +", а также, побеседовав со старшими написать небольшое "
                orangeSpan { +"эссе" }
                +", рассказывающее о судьбе родных и близких в годы войны (по желанию)."
            }
            indentedP {
                +"В процессе творчества, на основе услышанного и осознанного исторического материала, участники вовлекаются в живой диалог поколений, узнают и осознают историю своей семьи и своей страны."
            }
            h3 {
                +"Участники"
            }
            indentedP {
                orangeSpan { +"дети" }
                +" (обучающиеся дошкольных отделений образовательных комплексов, учащиеся общеобразовательных и специализированных школ, учреждений дополнительного образования детей, читатели детских библиотек)"
            }
            indentedP {
                orangeSpan { +"молодёжь" }
                +" (кадеты, воспитанники подростково-молодежных центров и клубов, члены молодежных общественных организаций, индивидуальные участники)."
            }
            h3 {
                +"Сроки"
            }
            styledP {
                +"В 2020 году Акция проводится с "
                orangeSpan { +"23.02.2020" }
                +" (День Защитника Отечества) по "
                orangeSpan { +"01.05.2019" }
                +". Оглашение результатов 09.05.2020 (в День Победы)."
            }
//            indentedP {
//                orangeSpan { +"Первый этап" }
//                +" (09.05.2019 г. – 20.10.2019 г.) информирование участников, загрузка конкурсных работ на официальный сайт Акции;"
//            }
//            indentedP {
//                orangeSpan { +"Второй этап" }
//                +" (21.10.2019 г. – 01.11.2019 г.) работа Жюри, определение финалистов, информирование о результатах проведения Акции;"
//            }
//            indentedP {
//                orangeSpan { +"Третий этап" }
//                +" (в преддверии Дня народного единства) награждение лауреатов и активных участников Акции, демонстрация работ финалистов на выставке"
//            }
            h3 {
                +"Участвуй"
            }
            +"Для участия в Конкурсе необходимо:"
            indentedP {
                +"Внимательно прочитать "
                a(href = FileInfo.officialPDF) {
                    +"Положение"
                }
                +" и познакомиться с материалами сайта; изучить литературу, иллюстративные и справочные материалы в разделе сайта «Наша Победа»"
            }
            indentedP {
                +"Подготовить рисунок и при желании написать небольшой текст (эссе), рассказывающее о судьбе родных и близких в годы войны; загрузить работу, нажав кнопку "
                navLink("") {
                    +"«Принять участие»"
                }
                +" и заполнив анкету на сайте Акции."
            }
            indentedP {
                +"При успешной загрузке, участнику придет подтверждение на указанный адрес электронной почты и "
                orangeSpan { +"сертификат" }
                +" участника Акции."
            }
            h3 {
                +"Есть вопрос?"
            }
            indentedP {
                +"E-mail: ${General.eMail}"
            }
            indentedP {
                +"Официальные страницы Акции в социальных сетях:"
                br {}
                +"ВКонтакте: "
                a(href = "https://vk.com/${General.enName}/") {
                    +"vk.com/${General.enName}"
                }
                br {}
                +"Инстаграм: "
                a(href = "https://www.instagram.com/${General.enName}/") {
                    +"instagram.com/${General.enName}"
                }
            }
            indentedP {
                +"Общая координация Акции: Головачев Владимир Сергеевич, e-mail: vladgolovachev@yandex.ru"
            }
        }
    }
}