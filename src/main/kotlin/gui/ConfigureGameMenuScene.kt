package gui

import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.*
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
class ConfigureGameMenuScene(bonsaiApplication: BonsaiApplication) : MenuScene(1920,1080, ColorVisual(Color(PRIMARY_COLOUR))) , Refreshable {

    private val contentPlayerPane =
        Pane<UIComponent>(
            posX = 80,
            posY = 40,
            width = 1100,
            height = 1000,
            visual = ColorVisual(Color(SECONDARY_COLOUR)).apply {
                style.borderRadius = BorderRadius(20.0)
            }
    )

    private val contentGoalTilePane =
        Pane<UIComponent>(
            posX = 1260,
            posY = 40,
            width = 580,
            height = 1000,
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

    private val titleGoalTileLabel =
        Label(
            posX = 20,
            posY = 20,
            width = 500,
            height = 100,
            text = "CHOOSE 3",
            font = Font(42, Color(0x000000), "ARIAL BLACK"),

            )

    private val titleGoalTileLabel2 =
        Label(
            posX = 20,
            posY = 120,
            width = 500,
            height = 100,
            text = "GOAL TILES",
            font = Font(42, Color(0x000000), "ARIAL BLACK"),

            )

    private val woodGoalTileLabel =
        LabelStyle2(
            posX = 20,
            posY = 270,
            text = "WOOD"
        )

    private val woodGoalTileButton = CheckBoxButton(
        posX = 390,
        posY = 270
    ).apply {
        onMouseClicked = {
            change()
        }
    }

    private val leafGoalTileLabel =
        LabelStyle2(
            posX = 20,
            posY = 410,
            text = "LEAF"
        )

    private val leafGoalTileButton = CheckBoxButton(
        posX = 390,
        posY = 410
    ).apply {
        onMouseClicked = {
            change()
        }
    }

    private val fruitGoalTileLabel =
        LabelStyle2(
            posX = 20,
            posY = 550,
            text = "FRUIT"
        )

    private val fruitGoalTileButton = CheckBoxButton(
        posX = 390,
        posY = 550
    ).apply {
        onMouseClicked = {
            change()
        }
    }

    private val flowerGoalTileLabel =
        LabelStyle2(
            posX = 20,
            posY = 690,
            text = "FLOWER"
        )

    private val flowerGoalTileButton = CheckBoxButton(
        posX = 390,
        posY = 690
    ).apply {
        onMouseClicked = {
            change()
        }
    }

    private val postionGoalTileLabel =
        LabelStyle2(
            posX = 20,
            posY = 830,
            text = "POSITION"
        )

    private val positionGoalTileButton = CheckBoxButton(
        posX = 390,
        posY = 830
    ).apply {
        onMouseClicked = {
            change()
        }
    }

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

    private val startButton = ButtonStyle2(
        posX = 715,
        posY = 830,
        text = "START"
    )

    private val backButton = ButtonStyle2(
        posX = 175,
        posY = 830,
        text = "BACK"
    ).apply {
        onMouseClicked = {
            bonsaiApplication.hideMenuScene()
            bonsaiApplication.showMainMenuScene()
        }
    }

    private val playerOrderButton = ButtonStyle2(
        posX = 445,
        posY = 830,
        text = "SET TURNS"
    )

    private val playerTurn = TurnLabel(
        posX = 40,
        posY = 270 ,
    ).apply {
        text = "1"
    }

    private val playerInput = TextFieldStyle1(
        posX = 190 ,
        posY = 270 ,
        prompt = "INPUT NAME"
    )

    private val playerColour = ColourButton(
        posX = 600,
        posY = 280
    )

    private val playerRemove = SquareButton(
        posX = 680,
        posY = 270
    ).apply {
        // When the button is clicked, the first player is removed
        onMouseClicked = {
            removePlayer(0)
        }
    }

    private val playerEasyBot = CheckBoxButton(
        posX = 830,
        posY = 270
    ).apply {
        onMouseClicked = {
            change()
        }
    }

    private val playerHardBot = CheckBoxButton(
        posX = 980,
        posY = 270
    ).apply {
        onMouseClicked = {
            change()
        }
    }

    // Group all player inputs in lists to easily manage them

    private val playerTurns : MutableList<Label> = mutableListOf(playerTurn)
    private val playerInputs : MutableList<TextField> = mutableListOf(playerInput)
    private val playerColours : MutableList<Button> = mutableListOf(playerColour)
    private val playerRemoves : MutableList<Button> = mutableListOf(playerRemove)
    private val playerEasyBots : MutableList<CheckBoxButton> = mutableListOf(playerEasyBot)
    private val playerHardBots : MutableList<CheckBoxButton> = mutableListOf(playerHardBot)



    init {
        contentPlayerPane.addAll(
            titleLabel,
            addPlayerButton,
            backButton,
            playerOrderButton,
            startButton,
            playerTurn,
            playerInput ,
            playerRemove,
            playerColour,
            playerEasyBot,
            playerHardBot,
        )
        contentGoalTilePane.addAll(
            titleGoalTileLabel,
            titleGoalTileLabel2,
            woodGoalTileLabel,
            woodGoalTileButton,
            leafGoalTileLabel,
            leafGoalTileButton,
            fruitGoalTileLabel,
            fruitGoalTileButton,
            flowerGoalTileLabel,
            flowerGoalTileButton,
            postionGoalTileLabel,
            positionGoalTileButton,
        )
        addComponents(contentPlayerPane,contentGoalTilePane)

    }

    private fun addPlayer() {

        val currentIndex = playerInputs.size

        if (currentIndex >= 4) return

        val newPlayerTurn = TurnLabel(
            posX = 40,
            posY = 270 + 140 * currentIndex ,
        ).apply {
            text = "${currentIndex+1}"
        }

        val newPlayerInput = TextFieldStyle1(
            posX = 190 ,
            posY = 270 + 140 * currentIndex ,
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

        val newPlayerEasyBot = CheckBoxButton(
            posX = 830,
            posY = 270 + 140 * currentIndex,
        ).apply {
            onMouseClicked = {
                change()
            }
        }

        val newPlayerHardBot = CheckBoxButton(
            posX = 980,
            posY = 270 + 140 * currentIndex,
        ).apply {
            onMouseClicked = {
                change()
            }
        }

        contentPlayerPane.addAll(
            newPlayerInput,
            newPlayerTurn,
            newPlayerRemove,
            newPlayerColour,
            newPlayerEasyBot,
            newPlayerHardBot,
        )

        playerTurns.add(newPlayerTurn)
        playerInputs.add(newPlayerInput)
        playerRemoves.add(newPlayerRemove)
        playerColours.add(newPlayerColour)
        playerEasyBots.add(newPlayerEasyBot)
        playerHardBots.add(newPlayerHardBot)

        if (currentIndex == 3){
            addPlayerButton.isDisabled = true
            addPlayerButton.isVisible = false
        }

        addPlayerButton.posY += 140
    }

    private fun removePlayer(index : Int) {

        if (playerInputs.size <= 1) return

        contentPlayerPane.remove(playerTurns[index])
        contentPlayerPane.remove(playerColours[index])
        contentPlayerPane.remove(playerRemoves[index])
        contentPlayerPane.remove(playerEasyBots[index])
        contentPlayerPane.remove(playerHardBots[index])
        contentPlayerPane.remove(playerInputs[index])

        playerTurns.removeAt(index)
        playerColours.removeAt(index)
        playerRemoves.removeAt(index)
        playerEasyBots.removeAt(index)
        playerHardBots.removeAt(index)
        playerInputs.removeAt(index)


        for (i in index until playerInputs.size){
            playerTurns[i].posY -= 140
            playerColours[i].posY -= 140
            playerRemoves[i].posY -= 140
            playerEasyBots[i].posY -= 140
            playerHardBots[i].posY -= 140
            playerInputs[i].posY -= 140

            playerRemoves[i].onMouseClicked = {
                removePlayer(i)
            }
        }

        addPlayerButton.isVisible = true
        addPlayerButton.isDisabled = false
        addPlayerButton.posY -= 140

    }
}
