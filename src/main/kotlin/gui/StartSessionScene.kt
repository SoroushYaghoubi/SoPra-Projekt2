package gui

import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual

class StartSessionScene(bonsaiApplication: BonsaiApplication) : MenuScene(
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


    private val backButton =
        ButtonStyle1(
            posX = 875,
            posY = 410,
            width = 280,
            height = 110,
            text = "BACK",
        ).apply {
            onMouseClicked = {
                bonsaiApplication.hideMenuScene()
                bonsaiApplication.showMainMenuScene()
            }
        }

    private val startSessionButton =
        ButtonStyle1(
            posX = 1195,
            posY = 410,
            width = 280,
            height = 110,
            text = "START SESSION",
        ).apply {
            onMouseClicked = {
                // TODO
                bonsaiApplication.hideMenuScene()

            }
        }

    init {
        addComponents(
            titleLabel,
            sessionTextField,
            startSessionButton,
            backButton,
        )
    }

}
