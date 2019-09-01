package sample

import react.RComponent
import react.RProps
import react.RState

interface GalleryBoxProps : RProps {
    val content: MutableList<String>
    val horizontalAmount: Int
}

abstract class GalleryBox : RComponent<GalleryBoxProps, RState>() {
    override fun render() {

    }
}