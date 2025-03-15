package gui

import entity.ColorType
import entity.GoalTileType
import entity.PlayerType
import service.RootService
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
import util.*



/**
 * The [ConfigureGameMenuScene] is a [MenuScene] to configure the starting parameters
 * of a game of bonsai in hot seat mode
 */
class ConfigureGameMenuScene(private val bonsaiApplication: BonsaiApplication,
                             private val rootService: RootService) : MenuScene(1920,1080, ColorVisual(Color(PRIMARY_COLOUR))) , Refreshable {

    private val playerColors = mutableListOf(
        ColorType.RED,
        ColorType.PURPLE,
        ColorType.BLACK,
        ColorType.BLUE
    )

    private val availableColors = mutableListOf(
        ColorType.RED,
        ColorType.PURPLE,
        ColorType.BLACK,
        ColorType.BLUE
    )

    private val colorMapping = mapOf(
        ColorType.RED to COLOUR_RED,
        ColorType.PURPLE to COLOUR_PURPLE,
        ColorType.BLACK to COLOUR_BLACK,
        ColorType.BLUE to COLOUR_BLUE
    )

    private fun nextColor(currentColor: ColorType): ColorType {
        val currentIndex = availableColors.indexOf(currentColor)
        return if (currentIndex == -1 || currentIndex == availableColors.lastIndex)
            availableColors.first()
        else
            availableColors[currentIndex + 1]
    }

    private fun assignColorButtonFunctionality() {
        playerColours.forEachIndexed { index, button ->
            button.onMouseClicked = {
                val currentColor = playerColors[index]
                val newColor = nextColor(currentColor)
                playerColors[index] = newColor

                // Update the button visual and ensure it stays rounded
                button.visual = ColorVisual(Color(colorMapping[newColor] ?: COLOUR_BLACK)).apply {
                    style.borderRadius = BorderRadius(20.0)
                }
            }
        }
    }

    private val selectedGoalTiles = mutableListOf<GoalTileType>()
    //private val orderedPlayer
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
            toggleGoalTile(GoalTileType.BROWN, this)
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
            toggleGoalTile(GoalTileType.GREEN, this)
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
            toggleGoalTile(GoalTileType.ORANGE, this)
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
            toggleGoalTile(GoalTileType.PINK, this)
        }
    }

    private val positionGoalTileLabel =
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
            toggleGoalTile(GoalTileType.BLUE, this)
        }
    }

    private fun toggleGoalTile(goalTileType: GoalTileType, button: CheckBoxButton) {
        if (button.isChecked) {
            if (selectedGoalTiles.size >= 3) {
                // If 3 goal tiles are already selected, we deselect the first one
                val firstSelected = selectedGoalTiles.first()
                val firstButton = when (firstSelected) {
                    GoalTileType.BROWN -> woodGoalTileButton
                    GoalTileType.GREEN -> leafGoalTileButton
                    GoalTileType.ORANGE -> fruitGoalTileButton
                    GoalTileType.PINK -> flowerGoalTileButton
                    GoalTileType.BLUE -> positionGoalTileButton
                }
                firstButton.change()
                selectedGoalTiles.remove(firstSelected)
            }
            selectedGoalTiles.add(goalTileType)
        } else {
            // If the button is being unchecked, remove the goal tile from the selected list
            selectedGoalTiles.remove(goalTileType)
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

    val startButton = ButtonStyle2(
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
            //TODO()
            //bonsaiApplication.hideMenuScene()
            //bonsaiApplication.showMainMenuScene()
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
            if (!isChecked) {
                if (playerHardBots[0].isChecked) {
                    playerHardBots[0].change()
                }
            }
            change()
        }
    }

    private val playerHardBot = CheckBoxButton(
        posX = 980,
        posY = 270
    ).apply {
        onMouseClicked = {
            println("Player 1 HardBot clicked. Current state: $isChecked")
            if (!isChecked) {
                println("Checking Player 1 HardBot. Unchecking Player 1 EasyBot if it is checked.")
                if (playerEasyBots[0].isChecked) {
                    playerEasyBots[0].change()
                    println("Player 1 EasyBot unchecked.")
                }
            }
            change()
            println("New Player 1 HardBot state: $isChecked")
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
            startButton.apply {
                onMouseClicked = {
                    val guiPlayer = playerInputs.mapIndexed() { index, it->
                        val color = playerColors[index]
                        val playerType = when {
                            playerEasyBots[index].isChecked -> PlayerType.EASYBOT
                            playerHardBots[index].isChecked -> PlayerType.HARDBOT
                            else -> PlayerType.HUMAN
                        }
                        entity.Player(it.text.trim(), playerType, true, color)
                    }.toMutableList()

                    rootService.gameService.startNewGame(guiPlayer, false,
                        selectedGoalTiles)
                    bonsaiApplication.hideMenuScene()
                    bonsaiApplication.showGameScene()
                }
            },
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
            positionGoalTileLabel,
            positionGoalTileButton,
        )
        addComponents(contentPlayerPane,contentGoalTilePane)

        assignColorButtonFunctionality()

    }

    private fun addPlayer() {
        val currentIndex = playerInputs.size
        if (currentIndex >= 4) return

        val newPlayerTurn = TurnLabel(
            posX = 40,
            posY = 270 + 140 * currentIndex,
        ).apply {
            text = "${currentIndex + 1}"
        }

        val newPlayerInput = TextFieldStyle1(
            posX = 190,
            posY = 270 + 140 * currentIndex,
            prompt = "INPUT NAME"
        )

        val newPlayerColour = ColourButton(
            posX = 600,
            posY = 280 + 140 * currentIndex,
        ).apply {
            onMouseClicked = {
                val currentColor = playerColors[currentIndex]
                val newColor = nextColor(currentColor)
                playerColors[currentIndex] = newColor

                // Apply the new color AND ensure the button stays rounded
                this.visual = ColorVisual(Color(colorMapping[newColor] ?: COLOUR_BLACK)).apply {
                    style.borderRadius = BorderRadius(20.0)  // Keep corners rounded
                }
            }

            // Ensure the initial state is also rounded
            visual = ColorVisual(Color(colorMapping[playerColors[currentIndex]] ?: COLOUR_BLACK)).apply {
                style.borderRadius = BorderRadius(20.0)
            }
        }

        val newPlayerRemove = SquareButton(
            posX = 680,
            posY = 270 + 140 * currentIndex,
        ).apply {
            onMouseClicked = {
                removePlayer(currentIndex)
            }
        }

        val newPlayerEasyBot = CheckBoxButton(
            posX = 830,
            posY = 270 + 140 * currentIndex,
        ).apply {
            onMouseClicked = {
                if (!isChecked) {
                    // If hardBot is checked, uncheck it
                    if (playerHardBots[currentIndex].isChecked) {
                        playerHardBots[currentIndex].change()
                    }
                }
                change()
            }
        }

        val newPlayerHardBot = CheckBoxButton(
            posX = 980,
            posY = 270 + 140 * currentIndex,
        ).apply {
            onMouseClicked = {
                if (!isChecked) {
                    // If easyBot is checked, uncheck it
                    if (playerEasyBots[currentIndex].isChecked) {
                        playerEasyBots[currentIndex].change()
                    }
                }
                change()
            }
        }

        // Add components to the pane
        contentPlayerPane.addAll(
            newPlayerInput,
            newPlayerTurn,
            newPlayerRemove,
            newPlayerColour,
            newPlayerEasyBot,
            newPlayerHardBot,
        )

        // Add to respective lists
        playerTurns.add(newPlayerTurn)
        playerInputs.add(newPlayerInput)
        playerColours.add(newPlayerColour)
        playerRemoves.add(newPlayerRemove)
        playerEasyBots.add(newPlayerEasyBot)
        playerHardBots.add(newPlayerHardBot)

        if (currentIndex == 3) {
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
