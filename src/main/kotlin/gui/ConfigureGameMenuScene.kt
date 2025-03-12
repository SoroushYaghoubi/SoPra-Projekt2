package gui

import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.util.Font
import util.SECONDARY_COLOUR


/**
 * The [ConfigureGameMenuScene] is a [MenuScene] to configure the starting parameters
 * of a game of bonsai in hot seat mode
 */
class ConfigureGameMenuScene : MenuScene(1920,1080, ColorVisual(Color(0xFFFFFF))) , Refreshable {

    private val contentPlayerPane =
        Pane<UIComponent>(
            posX = 80,
            posY = 80,
            width = 1100,
            height = 920,
            visual = ColorVisual(Color(SECONDARY_COLOUR)).apply {
                style.borderRadius = BorderRadius(20.0)
            }
    )

    private val titleLabel =
        Label(
            posX = 20,
            posY = 20,
            width = 1000,
            height = 100,
            text = "CONFIGURE GAME",
            font = Font(72, Color(0x000000), "ARIAL BLACK"),
        )

    private val playerDefaultTurn =
        TurnLabel(
            posX = 40,
            posY = 270,
        )

    private val playerDefaultInput =
        TextFieldStyle1(
            posX = 190 ,
            posY = 270 ,
            text = "NAME" ,
            prompt = "PLAYER NAME"
        )

    private val defaultEasyBotCheckBox =
        CheckBoxStyle1(
            posX = 830,
            posY = 270,
        )

    private val defaultHardBotCheckBox =
        CheckBoxStyle1(
            posX = 980,
            posY = 270,
        )

    private val playerRemoveButton =
        SquareButton(
            posX = 680,
            posY = 270,
        )

    init {
        contentPlayerPane.addAll(
            titleLabel,
            playerDefaultTurn,
            playerDefaultInput,
            defaultEasyBotCheckBox,
            defaultHardBotCheckBox,
            playerRemoveButton,
        )
        addComponents(contentPlayerPane)
    }
}
