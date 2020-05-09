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
                    +"РИСУЕМ ПОБЕДУ-2020: ИТОГИ АКЦИИ"
                }
            }
            styledP {
                +"Дорогие участники Всероссийской акции «Рисуем Победу-2020»!"
            }
            styledP {
                +"Более 27000 творческих работ из 2430 населённых пунктов нашей страны и 14 зарубежных государств было направлено для участия в Акции в этом году."
            }
            styledP {
                +"Всероссийская детско-юношеская акция «Рисуем Победу» стала самой масштабной детско-юношеской онлайн-акцией, посвящённой 75-летию Победы в Великой Отечественной войне."
            }
            styledP {
                +"Организационный комитет поздравляет Вас с Днем Великой Победы и сердечно благодарит всех, кто откликнулся и подготовил прекрасные, неравнодушные творческие работы, посвященные нашей Победе в Великой Отечественной войне!"
            }
            styledP {
                +"Мы благодарим всех авторов работ, а также их наставников и преподавателей!"
            }
            styledP {
                +"Жюри Акции особо отметило следующих участников:"
            }


            styledP {
                css {
                }
                orangeSpan {
                    +"Лауреаты Всероссийской акции «Рисуем Победу-2020»:"
                }
            }


            styledP {
                +"Младшая возрастная категория"
            }
            indentedP {
                +"1 место"
            }
            styledP {
                +"Антонян Милана, 6 лет (г. Красноярск)"
            }
            indentedP {
                +"2 место"
            }
            styledP {
                +"Захарова Александра, 6 лет (с.Терентьевское, Кемеровская область)"
            }
            indentedP {
                +"3 место"
            }
            styledP {
                +"Белков Евгений, 3 года (г. Канаш, Чувашская Республика)"
            }


            styledP {
                +"Средняя возрастная категория"
            }
            indentedP {
                +"1 место"
            }
            styledP {
                +"Мухтаров Карим, 9 лет (г. Москва)"
            }
            indentedP {
                +"2 место"
            }
            styledP {
                +"Дэли Анастасия, 9 лет (г. Тирасполь)"
            }
            indentedP {
                +"3 место"
            }
            styledP {
                +"Семенова Анастасия, 11 лет (г. Морозовск)"
            }


            styledP {
                +"Старшая возрастная категория"
            }
            indentedP {
                +"1 место"
            }
            styledP {
                +"Фадеева Александра , 15 лет (г. Сергиев Посад)"
            }
            indentedP {
                +"2 место"
            }
            styledP {
                +"Зуева Софья, 14 лет (г. Кранодар)"
            }
            indentedP {
                +"3 место"
            }
            styledP {
                +"Демина Увгения, 14 лет (г. Сызрань)"
            }



            styledP {
                orangeSpan {
                    +"Дипломанты"
                }
            }
            styledP {
                +"Волошина Софья, 13 лет (г. Краснодр)"
            }
            styledP {
                +"Твердовская Валерия, 13 лет (г.Ростов-на-Дону)"
            }
            styledP {
                +"Фадеева Александра , 15 лет (г. Сергиев Посад)"
            }
            styledP {
                +"Корепанова Полина, 12 лет (пос. Игра, Удмуртская Республика)"
            }
            styledP {
                +"Красилова Анастасия, 14 лет (г.Ростов-на-Дону)"
            }
            styledP {
                +"Кабанова Анна , 13 лет (г. Радужный, ХМАО-Югра)"
            }
            styledP {
                +"Писарева Анастасия, 14 лет (г. Нижний Новгород)"
            }
            styledP {
                +"Усачева Мира, 7 лет город (г. Красноярск)"
            }
            styledP {
                +"Суслова Кристина, 9 лет ( г. Шумерля)"
            }
            styledP {
                +"Бахурова Лика, 9 лет (г. Краснодар)"
            }
            styledP {
                +"Самойлов Никита , 9 лет (г. Сергиев Посад)"
            }
            styledP {
                +"Турутина Полина, 7 лет ( р.п.Мулловка, Ульяновская область)"
            }
            styledP {
                +"Костардина Ева, 7 лет (г. Рыбинск)"
            }
            styledP {
                +"Панова Ирина, 11 лет (г. Тирасполь)"
            }
            styledP {
                +"Легошина Ксения, 9 лет (г. Радужный, ХМАО-Югра)"
            }


            styledP {
                +"В ближайшее время лауреатам и финалистам будут направлены памятные дипломы."
            }
            styledP {
                +"Мы приняли решение не закрывать возможность дальнейшей загрузки работ на сайт http://risuem-pobedu.ru/ и получения сертификатов! Будем рады вашему творчеству!"
            }
            styledP {
                +"Приглашаем также принять участие и получить сертификат в онлайн-конкурса \"Краски Чувашии\" https://kraski-chuvashii.ru/"
            }
            styledP {
                +"Сердечно поздравляем наших лауреатов, финалистов и всех участников Акции!"
            }
            styledP {
                +"#РисуемПобеду"
            }
            styledImg(src = "/images/design/post3.jpg") {
                css {
                    width = 100.pct
                }
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