package gui

import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual


/**
 * The [JoinScene] is a [MenuScene] to join an online session for a game of bonsai
 *
 *
 */
class JoinScene(bonsaiApplication: BonsaiApplication) : MenuScene(
    1920,1080, ImageVisual("Backgrounds/Hintergrund2.png",1920,1080)
) , Refreshable {

    private val titleLabel = Label(
        0,
        0,
        800,
        210,
        "BONSAI",
        font = Font(164.0, Color(0x000000), "Arial Black", Font.FontWeight.BOLD),)

    private val sessionTextField =
            TextFieldStyle2(
                posX = 875,
                posY = 275,
                text = "",
                prompt = "SESSION ID",
            )


    private val nameTextField =
        TextFieldStyle2(
            posX = 875,
            posY = 410,
            text = "",
            prompt = "NAME",
        )

    private val easyBotButton =
        CheckBoxButton2(
            posX = 1500,
            posY = 410,
            text = "EASY BOT",
        ).apply {
            onMouseClicked = {
                change()
            }
        }

    private val hardBotButton =
        CheckBoxButton2(
            posX = 1635,
            posY = 410,
            text = "HARD BOT",
        ).apply {
            onMouseClicked = {
                change()
            }
        }

    private val backButton =
        ButtonStyle1(
            posX = 875,
            posY = 545,
            width = 280,
            height = 110,
            text = "BACK",
        ).apply {
            onMouseClicked = {
                bonsaiApplication.hideMenuScene()
                bonsaiApplication.showMainMenuScene()
            }
        }

    private val joinButton =
        ButtonStyle1(
            posX = 1175,
            posY = 545,
            width = 280,
            height = 110,
            text = "JOIN",
        ).apply {
            onMouseClicked = {
                // TODO
                bonsaiApplication.hideMenuScene()
                bonsaiApplication.showWaitingScene()
            }
        }

    init {
        addComponents(
            titleLabel,
            sessionTextField,
            nameTextField,
            easyBotButton,
            hardBotButton,
            joinButton,
            backButton,
        )
    }

}
