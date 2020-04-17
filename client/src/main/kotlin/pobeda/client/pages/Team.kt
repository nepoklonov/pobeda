package pobeda.client.pages

import kotlinx.css.*
import pobeda.client.send
import pobeda.client.stucture.StandardPageComponent
import pobeda.client.stucture.YamlState
import pobeda.client.stucture.updateYamlState
import pobeda.common.Request
import pobeda.common.Team
import pobeda.common.info.FileInfo
import pobeda.common.interpretation.YamlRef
import react.dom.h3
import react.dom.p
import styled.*

class TeamComponent : StandardPageComponent<YamlState<Team>>() {
    init {
        state.yaml = Team(emptyList(), emptyList())
        Request.GetYaml(YamlRef.TeamYaml).send(Team.serializer(), ::updateYamlState)
    }

    override fun StyledDOMBuilder<*>.page() {
        css {
            width = 100.pct
            height = 100.pct
        }
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
                                    flexShrink = 0.0
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