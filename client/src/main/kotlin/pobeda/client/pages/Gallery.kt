package pobeda.client.pages

import pobeda.client.GalleryBox
import pobeda.client.stucture.StandardPageComponent
import pobeda.client.stucture.YamlListState
import pobeda.client.stucture.initYamlListState
import pobeda.client.updateYaml
import styled.StyledDOMBuilder


class GalleryComponent : StandardPageComponent<YamlListState<String>>() {

    object BoxProps {
        const val horizontalAmount = 2
        const val proportion = 0.75
        const val zoom = 0.8
    }

    init {
        initYamlListState()
        updateYaml(0, 0)
    }


    override fun StyledDOMBuilder<*>.page() {
        child(GalleryBox::class) {
            attrs {
                content = state.yaml
                horizontalAmount = BoxProps.horizontalAmount
                proportion = BoxProps.proportion
                zoom = BoxProps.zoom
                getImages = { w, h -> updateYaml(w, h) }
            }
        }
    }
}