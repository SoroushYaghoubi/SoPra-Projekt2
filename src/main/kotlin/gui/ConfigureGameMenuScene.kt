package gui

import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import util.PRIMARY_COLOUR
import util.SECONDARY_COLOUR
import util.TERTIARY_COLOUR


/**
 * The [ConfigureGameMenuScene] is a [MenuScene] to configure the starting parameters
 * of a game of bonsai in hot seat mode
 */
class ConfigureGameMenuScene : MenuScene(1920,1080, ColorVisual(Color(PRIMARY_COLOUR))) , Refreshable {

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
            prompt = "INPUT NAME"
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
        ).apply {
            // When the button is clicked, the first player is removed
            onMouseClicked = {
                removePlayer(0)
            }
        }

    private val playerDefaultColour =
        ColourButton(
            posX = 600,
            posY = 280,
        )

    private val addPlayerButton =
        Button(
            width = 110,
            height = 110,
            posX = 435,
            posY = 410,
            visual = CompoundVisual(
                ColorVisual(Color(TERTIARY_COLOUR)).apply {
                    style.borderRadius = BorderRadius(20.0)
                },
                ImageVisual("add.png")
            ) ,
        ).apply {
            onMouseClicked = {
                addPlayer()
            }
        }



    // Group all player inputs in lists to easily manage them
    private val playerInputs = mutableListOf(playerDefaultInput)
    private val playerRemoves = mutableListOf(playerRemoveButton)
    private val playerTurns = mutableListOf(playerDefaultTurn)
    private val playerColours = mutableListOf(playerDefaultColour)
    private val playerEasyBot = mutableListOf(defaultEasyBotCheckBox)
    private val playerHardBot = mutableListOf(defaultHardBotCheckBox)



    init {
        contentPlayerPane.addAll(
            titleLabel,
            playerDefaultTurn,
            playerDefaultInput,
            defaultEasyBotCheckBox,
            defaultHardBotCheckBox,
            playerRemoveButton,
            playerDefaultColour ,
            addPlayerButton
        )
        addComponents(contentPlayerPane)
    }

    private fun addPlayer() {

        val currentIndex = playerInputs.size

        if (currentIndex >= 4) return

        val newPlayerTurn = TurnLabel(
            posX = 40,
            posY = 270 + 140 * currentIndex ,
        )

        val newPlayerInput = TextFieldStyle1(
            posX = 190 ,
            posY = 270 + 140 * currentIndex ,
            text = "NAME" ,
            prompt = "INPUT NAME"
        )

        val newPlayerColour = ColourButton(
            posX = 600,
            posY = 280 + 140 * currentIndex ,
        )

        val newPlayerRemove = SquareButton(
            posX = 680,
            posY = 270 + 140 * currentIndex ,
        ).apply {
            // When the button is clicked, the first player is removed
            onMouseClicked = {
                removePlayer(currentIndex)
            }
        }

        val newPlayerEasyBot = CheckBoxStyle1(
            posX = 830,
            posY = 270 + 140 * currentIndex,
        )

        val newPlayerHardBot = CheckBoxStyle1(
            posX = 980,
            posY = 270 + 140 * currentIndex,
        )

        contentPlayerPane.addAll(
            newPlayerTurn,
            newPlayerInput,
            newPlayerRemove,
            newPlayerColour,
            newPlayerEasyBot,
            newPlayerHardBot,
            )

        playerTurns.add(newPlayerTurn)
        playerInputs.add(newPlayerInput)
        playerRemoves.add(newPlayerRemove)
        playerColours.add(newPlayerColour)
        playerEasyBot.add(newPlayerEasyBot)
        playerHardBot.add(newPlayerHardBot)

        if (currentIndex == 3){
            addPlayerButton.isDisabled = true
            addPlayerButton.isVisible = false
        }

        addPlayerButton.posY += 140
    }

    private fun removePlayer(index : Int) {

        if (playerInputs.size <= 1) return

        contentPlayerPane.remove(playerTurns[index])
        contentPlayerPane.remove(playerInputs[index])
        contentPlayerPane.remove(playerColours[index])
        contentPlayerPane.remove(playerRemoves[index])
        contentPlayerPane.remove(playerEasyBot[index])
        contentPlayerPane.remove(playerHardBot[index])

        playerTurns.removeAt(index)
        playerInputs.removeAt(index)
        playerColours.removeAt(index)
        playerRemoves.removeAt(index)
        playerEasyBot.removeAt(index)
        playerHardBot.removeAt(index)

        for (i in index until playerInputs.size){
            playerTurns[i].posY -= 140
            playerInputs[i].posY -= 140
            playerColours[i].posY -= 140
            playerRemoves[i].posY -= 140
            playerRemoves[i].onMouseClicked = {
                removePlayer(i)
            }
            playerEasyBot[i].posY -= 140
            playerHardBot[i].posY -= 140
        }

        addPlayerButton.isVisible = true
        addPlayerButton.isDisabled = false
        addPlayerButton.posY -= 140

    }
}
