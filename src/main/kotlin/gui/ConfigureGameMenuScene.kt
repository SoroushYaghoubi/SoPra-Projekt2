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
            posY = 80,
            width = 500,
            height = 100,
            text = "GOAL TILES",
            font = Font(42, Color(0x000000), "ARIAL BLACK"),

            )

    private val woodGoalTileLabel =
        LabelStyle2(
            posX = 20,
            posY = 200,
            text = "WOOD"
        )

    private val woodGoalTileButton = CheckBoxButton(
        posX = 390,
        posY = 200
    ).apply {
        onMouseClicked = {
            change()
            toggleGoalTile(GoalTileType.BROWN, this)
        }
    }

    private val leafGoalTileLabel =
        LabelStyle2(
            posX = 20,
            posY = 330,
            text = "LEAF"
        )

    private val leafGoalTileButton = CheckBoxButton(
        posX = 390,
        posY = 330
    ).apply {
        onMouseClicked = {
            change()
            toggleGoalTile(GoalTileType.GREEN, this)
        }
    }

    private val fruitGoalTileLabel =
        LabelStyle2(
            posX = 20,
            posY = 460,
            text = "FRUIT"
        )

    private val fruitGoalTileButton = CheckBoxButton(
        posX = 390,
        posY = 460
    ).apply {
        onMouseClicked = {
            change()
            toggleGoalTile(GoalTileType.ORANGE, this)
        }
    }

    private val flowerGoalTileLabel =
        LabelStyle2(
            posX = 20,
            posY = 590,
            text = "FLOWER"
        )

    private val flowerGoalTileButton = CheckBoxButton(
        posX = 390,
        posY = 590
    ).apply {
        onMouseClicked = {
            change()
            toggleGoalTile(GoalTileType.PINK, this)
        }
    }

    private val positionGoalTileLabel =
        LabelStyle2(
            posX = 20,
            posY = 720,
            text = "POSITION"
        )

    private val positionGoalTileButton = CheckBoxButton(
        posX = 390,
        posY = 720
    ).apply {
        onMouseClicked = {
            change()
            toggleGoalTile(GoalTileType.BLUE, this)
        }
    }

    private val randomGoalTileLabel =
        LabelStyle2(
            posX = 20,
            posY = 870,
            text = "RANDOM"
        )

    private val randomGoalTileButton = CheckBoxButton(
        posX = 390,
        posY = 870
    ).apply {
        onMouseClicked = {
            clearSelectedGoalTiles()
            selectRandomGoalTiles()
        }
    }

    private fun selectRandomGoalTiles() {
        val allGoalTiles = GoalTileType.entries.shuffled()
        val selectedTiles = allGoalTiles.take(3)

        for (goalTile in selectedTiles) {
            val button = getGoalTileButton(goalTile)
                button.change()

            selectedGoalTiles.add(goalTile)
        }
    }

    private fun clearSelectedGoalTiles() {
        for (goalTile in selectedGoalTiles) {
            val button = getGoalTileButton(goalTile)
            if (button.isChecked) {
                button.change()
            }
        }
        selectedGoalTiles.clear()
    }

    private fun getGoalTileButton(goalTileType: GoalTileType): CheckBoxButton {
        return when (goalTileType) {
            GoalTileType.BROWN -> woodGoalTileButton
            GoalTileType.GREEN -> leafGoalTileButton
            GoalTileType.ORANGE -> fruitGoalTileButton
            GoalTileType.PINK -> flowerGoalTileButton
            GoalTileType.BLUE -> positionGoalTileButton
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
    ).apply {
        onMouseClicked = {
            //TODO
        }
    }

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
        text = "RANDOM ORDER"
    ).apply {
        onMouseClicked = {
            randomizePlayerOrder()
        }
    }

    private fun randomizePlayerOrder() {
        val indices = playerInputs.indices.toList().shuffled()

        val shuffledInputs = indices.map { playerInputs[it] }.toMutableList()
        val shuffledColors = indices.map { playerColors[it] }.toMutableList()
        val shuffledEasyBots = indices.map { playerEasyBots[it] }.toMutableList()
        val shuffledHardBots = indices.map { playerHardBots[it] }.toMutableList()
        val shuffledTurns = indices.map { playerTurns[it] }.toMutableList()
        val shuffledColours = indices.map { playerColours[it] }.toMutableList()
        val shuffledRemoves = indices.map { playerRemoves[it] }.toMutableList()

        playerInputs.clear()
        playerInputs.addAll(shuffledInputs)

        playerColors.clear()
        playerColors.addAll(shuffledColors)

        playerEasyBots.clear()
        playerEasyBots.addAll(shuffledEasyBots)

        playerHardBots.clear()
        playerHardBots.addAll(shuffledHardBots)

        playerTurns.clear()
        playerTurns.addAll(shuffledTurns)

        playerColours.clear()
        playerColours.addAll(shuffledColours)

        playerRemoves.clear()
        playerRemoves.addAll(shuffledRemoves)

        rebindBotHandlers()

        updatePlayerPositions()
    }

    private fun rebindBotHandlers() {
        for (i in playerEasyBots.indices) {
            playerEasyBots[i].onMouseClicked = {
                if (!playerEasyBots[i].isChecked) {
                    if (playerHardBots[i].isChecked) {
                        playerHardBots[i].change()
                    }
                }
                playerEasyBots[i].change()
            }

            playerHardBots[i].onMouseClicked = {
                if (!playerHardBots[i].isChecked) {
                    if (playerEasyBots[i].isChecked) {
                        playerEasyBots[i].change()
                    }
                }
                playerHardBots[i].change()
            }
        }
    }

    private val playerTurn = TurnLabel(
        posX = 40,
        posY = 270,
    ).apply {
        text = "1"
        onMouseClicked = {
            swapPlayerWithNext(0)
        }
    }

    private fun swapPlayerWithNext(index: Int) {
        val nextIndex = if (index == playerInputs.size - 1) 0 else index + 1

        println("Tausche Spieler ${index + 1} mit Spieler ${nextIndex + 1}")

        swapInList(playerInputs, index, nextIndex)
        swapInList(playerColors, index, nextIndex)
        swapInList(playerEasyBots, index, nextIndex)
        swapInList(playerHardBots, index, nextIndex)
        swapInList(playerTurns, index, nextIndex)
        swapInList(playerColours, index, nextIndex)
        swapInList(playerRemoves, index, nextIndex)

        updatePlayerPositions()
        updatePlayerClickHandlers()
    }

    private fun updatePlayerPositions() {
        for (i in playerInputs.indices) {

            playerTurns[i].posY = 270.0 + 140 * i
            playerInputs[i].posY = 270.0 + 140 * i
            playerColours[i].posY = 280.0 + 140 * i
            playerRemoves[i].posY = 270.0 + 140 * i
            playerEasyBots[i].posY = 270.0 + 140 * i
            playerHardBots[i].posY = 270.0 + 140 * i

            playerTurns[i].text = "${i + 1}"
        }
    }

    private fun updatePlayerClickHandlers() {
        for (i in playerTurns.indices) {
            playerTurns[i].onMouseClicked = {
                swapPlayerWithNext(i)
            }
        }
    }

    private fun <T> swapInList(list: MutableList<T>, index1: Int, index2: Int) {
        val temp = list[index1]
        list[index1] = list[index2]
        list[index2] = temp
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
            if (!isChecked) {
                if (playerEasyBots[0].isChecked) {
                    playerEasyBots[0].change()
                }
            }
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
            startButton.apply {
                onMouseClicked = {
                    val guiPlayer = playerInputs.mapIndexed() { index, it->
                        val color = playerColors[index]
                        val playerType = when {
                            playerEasyBots[index].isChecked -> PlayerType.EASYBOT
                            playerHardBots[index].isChecked -> PlayerType.HARDBOT
                            else -> PlayerType.HUMAN
                        }
                        println(it.text.trim())
                        val name =
                            if (it.text.trim() == "") {
                                when (index) {
                                    3 -> "Alice"
                                    2 -> "Bob"
                                    1 -> "Cody"
                                    else -> "Dirk"
                                }
                            }else {
                                    it.text.trim()

                            }
                        entity.Player(name, playerType, true, color)
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
            randomGoalTileLabel,
            randomGoalTileButton,
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
            onMouseClicked = {
                swapPlayerWithNext(currentIndex)
            }
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

                this.visual = ColorVisual(Color(colorMapping[newColor] ?: COLOUR_BLACK)).apply {
                    style.borderRadius = BorderRadius(20.0)
                }
            }

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
