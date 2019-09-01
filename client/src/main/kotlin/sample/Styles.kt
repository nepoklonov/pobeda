package sample

import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import styled.StyleSheet

val orangeBrightColor = Color("#f70")
val yellowStarColor = rgb(233, 200, 104)

object MainStyles : StyleSheet("main") {
    val orangeText by css {
        color = orangeBrightColor
    }
    val normalA by css {
        textDecoration = TextDecoration.none
        active {
            position = Position.relative
            top = (-1).px
        }
    }
    val hNavElement by css {
        paddingLeft = 5.px
        paddingRight = 5.px
        children("a") {
            +normalA
            color = Color.black
            hover {
                color = orangeBrightColor
            }
        }
    }
    val leftNavElement by css {
        children("a") {
            +normalA
            color = yellowStarColor
            fontWeight = FontWeight.bold
            fontSize = 20.px
            hover {
                color = orangeBrightColor
            }
        }
    }
    val current by css {
        backgroundColor = Color.orangeRed
    }
}