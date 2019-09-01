package sample.pages

import kotlinx.css.fontSize
import kotlinx.css.px
import react.RBuilder
import react.dom.a
import react.dom.br
import react.dom.h3
import react.dom.p
import react.router.dom.navLink
import sample.MainStyles
import sample.info.FileInfo
import sample.info.General
import sample.stucture.PageState
import sample.stucture.StandardPageComponent
import styled.StyledDOMBuilder
import styled.css
import styled.styledA
import styled.styledSpan

fun RBuilder.makeOrange(string: String) =
        styledSpan {
            css {
                +MainStyles.orangeText
            }
            +string
        }


class AboutComponent : StandardPageComponent<PageState>() {
    override fun StyledDOMBuilder<*>.page() {
        styledA(href = FileInfo.officialPDF) {
            css {
                fontSize = 14.px
            }
            +"Скачать Положение"
        }
        p {
            makeOrange("Акция")
            +" проводится ежегодно с 2012 года и охватывает все регионы России. Юным участникам предлагается создать"
            makeOrange("рисунок")
            +", а также, побеседовав со старшими написать небольшое "
            makeOrange("эссе")
            +", рассказывающее о судьбе родных и близких в годы войны (по желанию)."
        }
        p {
            +"В процессе творчества, на основе услышанного и осознанного исторического материала, участники вовлекаются в живой диалог поколений, узнают и осознают историю своей семьи и своей страны."
        }
        h3 {
            +"Участники"
        }
        p {
            makeOrange("дети")
            +" (обучающиеся дошкольных отделений образовательных комплексов, учащиеся общеобразовательных и специализированных школ, учреждений дополнительного образования детей, читатели детских библиотек)"
        }
        p {
            makeOrange("молодёжь")
            +" (кадеты, воспитанники подростково-молодежных центров и клубов, члены молодежных общественных организаций, индивидуальные участники)."
        }
        h3 {
            +"Сроки"
        }
        p {
            +"В 2019 году Акция проводится с "
            makeOrange("09.05.2019")
            +" (День Победы) по "
            makeOrange("04.11.2019")
            +" (День народного единства)."
        }
        p {
            makeOrange("Первый этап")
            +"(09.05.2019 г. – 20.10.2019 г.) информирование участников, загрузка конкурсных работ на официальный сайт Акции;"
        }
        p {
            makeOrange("Второй этап")
            +"(21.10.2019 г. – 01.11.2019 г.) работа Жюри, определение финалистов, информирование о результатах проведения Акции;"
        }
        p {
            makeOrange("Третий этап")
            +"(в преддверии Дня народного единства) награждение лауреатов и активных участников Акции, демонстрация работ финалистов на выставке"
        }
        h3 {
            +"Участвуй"
        }
        +"Для участия в Конкурсе необходимо:"
        p {
            +"Внимательно прочитать"
            a(href = FileInfo.officialPDF) {
                +"Положение"
            }
            +" и познакомиться с материалами сайта; изучить литературу, иллюстративные и справочные материалы в разделе сайта «Наша Победа»"
        }
        p {
            +"Подготовить рисунок и при желании написать небольшой текст (эссе), рассказывающее о судьбе родных и близких в годы войны.; загрузить работу, нажав кнопку "
            navLink("") {
                +"«Принять участие»"
            }
            +"и заполнив анкету на сайте Акции."
        }
        p {
            +"При успешной загрузке, участнику придет подтверждение на указанный адрес электронной почты и"
            makeOrange("сертификат")
            +"участника Акции."
        }
        h3 {
            +"Есть вопрос?"
        }
        p {
            +"E-mail: ${General.eMail}"
        }
        p {
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
        p {
            +"Общая координация Акции: Головачев Владимир Сергеевич, e-mail: vladgolovachev@yandex.ru"
        }
    }
}