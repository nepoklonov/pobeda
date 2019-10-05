package sample.pages

import kotlinx.css.*
import org.w3c.xhr.FormData
import react.dom.h3
import react.dom.p
import react.setState
import sample.Team
import sample.callAPI
import sample.info.FileInfo
import sample.json
import sample.stucture.StandardPageComponent
import sample.stucture.YamlState
import styled.*

class TeamComponent : StandardPageComponent<YamlState<Team>>() {
    init {
        val formData = FormData()
        formData.append("yaml", "team")
        callAPI("get-yaml", formData) {
            setState {
                yaml = json.parse(Team.serializer(), responseText)
            }
        }
    }

    override fun StyledDOMBuilder<*>.page() {
        css {
            width = 100.pct
            height = 100.pct
        }
        if (state.yaml != undefined) {
            for (i in 0 until state.yaml.positions.size) {
                styledDiv {
                    css {
                        margin(10.px, 30.px)
                    }
                    h3 {
                        +state.yaml.positions[i]
                    }
                    state.yaml.team.forEach {
                        if (it.position == i) {
                            styledDiv {
                                css {
                                    margin(10.px, 0.px)
                                    display = Display.flex
                                }
                                styledImg(src = FileInfo.Image.teamImagesDir
                                    + "/" + it.picture) {
                                    css {
                                        marginRight = 10.px
                                        maxWidth = 100.px
                                        maxHeight = 100.px
                                    }
                                }
                                styledDiv {
                                    p {
                                        +it.fullName
                                    }
                                    styledP {
                                        css {
                                            fontSize = 10.pt
                                            color = Color.darkGray
                                        }
                                        p {
                                            +it.description
                                        }
                                        p {
                                            +it.email
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}