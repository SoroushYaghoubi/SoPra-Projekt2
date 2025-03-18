package gui

import entity.*
import gui.BonsaiApplication
import service.RootService
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.HexagonGrid
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.TextVisual
import util.*

/**
 * The [BonsaiGameScene] is a [BoardGameScene] that displays the whole game and
 * lets the user play the bonsai game
 */
class BonsaiGameScene(private val rootService: RootService) :
    BoardGameScene(1920, 1080, ColorVisual(Color(PRIMARY_COLOUR))), Refreshable {

    private val treeTileMaps: MutableList<BidirectionalMap<Tile, HexagonView>> = mutableListOf()
    private val supplyTileMaps: MutableList<BidirectionalMap<Tile, HexagonView>> = mutableListOf()
    private val zenCardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    private val playerPanes: MutableList<Pane<ComponentView>> = mutableListOf()
    private val treeHexagonGrids: MutableList<HexagonGrid<HexagonView>> = mutableListOf()
    private val targetLayouts: MutableList<Pane<ComponentView>> = mutableListOf()
    private val treePanes: MutableList<ComponentView> = mutableListOf()
    private val woodSupplyDecks: MutableList<Area<HexagonView>> = mutableListOf()
    private val leafSupplyDecks: MutableList<Area<HexagonView>> = mutableListOf()
    private val flowerSupplyDecks: MutableList<Area<HexagonView>> = mutableListOf()
    private val fruitSupplyDecks: MutableList<Area<HexagonView>> = mutableListOf()
    private val playerButtons: MutableList<Button> = mutableListOf()
    private val goalButtons: MutableList<Button> = mutableListOf()
    private var goalTileList: MutableList<GoalTile> = mutableListOf()


    // button for cultivate
    private val cultivateButton =
        Button(
            posX = 190, posY = 270,
            width = 150,
            height = 80,
            visual = ColorVisual(Color(0xffffff)).apply {
                style.borderRadius = BorderRadius(20.0)
            },
            text = "Cultivate",
            font = Font(36)
        ).apply {
            onMouseClicked = {
                val game = rootService.currentGame?.currentBonsaiGameState
                checkNotNull(game)
                if (game.currentState == States.START_TURN ||
                    game.currentState == States.CHOOSE_ACTION
                ) {
                    rootService.playerActionService.cultivate()
                }
            }
        }

    // button for meditate
    private val endTurnButton =
        Button(
            posX = 360,
            posY = 270,
            width = 150,
            height = 80,
            visual = ColorVisual(Color(0xffffff)).apply {
                style.borderRadius = BorderRadius(20.0)
            },
            text = "EndTurn",
            font = Font(36)
        ).apply {
            onMouseClicked = {
                if (rootService.playerActionService.canEndTurn()) {
                    rootService.playerActionService.endTurn()

                }
            }
        }

    // button for remove from tree
    private val removeButton =
        Button(
            posX = 20,
            posY = 270,
            width = 150,
            height = 80,
            visual = ColorVisual(Color(0xffffff)).apply {
                style.borderRadius = BorderRadius(20.0)
            },
            text = "remove",
            font = Font(36)
        ).apply {
            // Testing to skip a player's turn
            onMouseClicked = {
                val game = rootService.currentGame?.currentBonsaiGameState
                checkNotNull(game)
                rootService.playerActionService.cultivate()
                rootService.playerActionService.endTurn()
            }
        }


    // text for how many cards there are
    private val cardSumText = Label(
        posX = 50, posY = 50,
        visual = ColorVisual(Color(0, 0, 0, 0)),
        text = "",
        font = Font(30, Color.BLACK)
    )

    // pane for the cards
    private val zenCardPane =
        Pane<ComponentView>(
            posX = 20, posY = 20,
            width = 700,
            height = 220,
            visual = ImageVisual("zenBoard.JPG").apply {
                style.borderRadius = BorderRadius(20.0)
            }
        )

    private val nameText = Label(
        posY = 10,
        width = 380,
        height = 60,
        text = "",
        font = Font(40, Color(TERTIARY_COLOUR))
    )

    // pane for player info
    // tiles capacity
    // play capacity
    private val infoPane =
        Pane<ComponentView>(
            posX = 737,
            posY = 20,
            width = 400,
            height = 220,
            visual = ColorVisual(Color(SECONDARY_COLOUR)).apply {
                style.borderRadius = BorderRadius(20.0)
            }
        ).apply {
            this.add(nameText)
        }

    // pane for collected card
    private val collectedCardPane =
        Pane<UIComponent>(
            posX = 1154,
            posY = 20,
            width = 400,
            height = 220,
            visual = ColorVisual(Color(SECONDARY_COLOUR)).apply {
                style.borderRadius = BorderRadius(20.0)
            }
        )

    private val interactionText = Label(
        posY = 20,
        width = 990,
        height = 60,
        text = "",
        font = Font(40, Color(TERTIARY_COLOUR))
    )

    // pane for the interaction
    private val interactionPane =
        Pane<ComponentView>(
            posX = 540,
            posY = 260,
            width = 1020,
            height = 100,
            visual = ColorVisual(Color(SECONDARY_COLOUR)).apply {
                style.borderRadius = BorderRadius(20.0)
            },
        ).apply {
            this.add(interactionText)
        }

    //zenDeck
    private val zenDeckView = CardStack<CardView>(
        posX = 60, posY = 40,
        width = 110, height = 160
    )

    //faceup cards
    private val faceUpCards = LinearLayout<CardView>(
        posX = 205,
        posY = 40,
        width = 500,
        height = 160,
        spacing = 13
    )

    private val undoButton =
        Button(
            posX = 20,
            posY = 30,
            width = 50,
            height = 40,
            visual = ColorVisual(Color(0xffffff)).apply {
                style.borderRadius = BorderRadius(20.0)
            },
            text = "⬅",
            font = Font(36)
        ).apply {
            onMouseClicked = {
                if (rootService.historyService.canUndo()) {
                    rootService.historyService.undo()
                }
            }
            //hide the button in network games
            isVisible = rootService.currentGame?.currentBonsaiGameState?.currentPlayer?.isLocal == true
        }

    /**
     * This just does not work at all, probable because relevant parts of the history are not deep copies
     * ToDo make redo and undo not broken
     */
    private val redoButton =
        Button(
            posX = 230,
            posY = 30,
            width = 50,
            height = 40,
            visual = ColorVisual(Color(0xffffff)).apply {
                style.borderRadius = BorderRadius(20.0)
            },
            text = "➡",
            font = Font(36)
        ).apply {
            onMouseClicked = {
                if (rootService.historyService.canRedo()) {
                    rootService.historyService.redo()
                }
            }
            //hide the button in network games
            isVisible = rootService.currentGame?.currentBonsaiGameState?.currentPlayer?.isLocal == true
        }

    override fun refreshAfterRedoOrUndo() {
        val gameState = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(gameState) { "Game state is not initialized." }

        // Refresh player name and supply amount
        nameText.text = "Player: ${gameState.currentPlayer.name}"
        updateSupplyAmount(gameState.currentPlayer)

        // Refresh the zen board
        faceUpCards.clear()
        zenDeckView.clear()
        zenCardMap.clear()

        gameState.faceUpCards.forEach { card ->
            val cardView = CardView(
                height = 160,
                width = 110,
                front = CompoundVisual(ColorVisual.WHITE, TextVisual("${card.id}")),
                back = ColorVisual.BLACK,
            )
            cardFrontSetter(card, cardView)
            cardView.showFront()
            faceUpCards.add(cardView)
            zenCardMap.add(card to cardView)
        }

        gameState.zenDeck.forEach { card ->
            val cardView = CardView(
                height = 160,
                width = 110,
                front = CompoundVisual(ColorVisual.WHITE, TextVisual("${card.id}")),
                back = ColorVisual.BLACK,
            )
            cardFrontSetter(card, cardView)
            cardView.showBack()
            zenDeckView.add(cardView)
            zenCardMap.add(card to cardView)
        }

        // Refresh supply tiles
        updateSupply(gameState.currentPlayer)

        // Refresh tree tiles
        createEmptyHex(gameState.currentPlayer)

        // Refresh interaction text
        interactionText.text = "Undo/Redo performed. Current player: ${gameState.currentPlayer.name}"
    }


    private val saveButton =
        Button(
            posX = 20, // Adjusted to fit within the buttonPane
            posY = 100, // Adjusted to fit within the buttonPane
            width = 260,
            height = 35,
            visual = ColorVisual(Color(0xffffff)).apply {
                style.borderRadius = BorderRadius(20.0)
            },
            text = "SAVE GAME",
            font = Font(36)
        ).apply {
            // hide the button in network game
            if(rootService.currentGame?.currentBonsaiGameState?.currentPlayer?.isLocal == true){
                isVisible = true
                onMouseClicked = {
                    rootService.historyService.saveGame()
                    //BonsaiApplication.showMenuScene()
                }
            }
        }

    // right side pane
    private val buttonPane = Pane<UIComponent>(
        posX = 1600,
        posY = 20,
        width = 300,
        height = 1040,
        visual = ColorVisual(Color(SECONDARY_COLOUR)).apply {
            style.borderRadius = BorderRadius(20.0)
        }
    ).apply {
        this.add(undoButton)
        this.add(redoButton)
        this.add(saveButton)
    }

    private fun createRightSidePane() {
        val gameState = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(gameState) { "Game state is not initialized." }

        // sorted goal tiles in order: brown, green, orange, pink, blue
        val sortedGoalTiles = gameState.goalTiles.sortedBy {
            when (it.goalTileType) {
                GoalTileType.BROWN -> 0
                GoalTileType.GREEN -> 1
                GoalTileType.ORANGE -> 2
                GoalTileType.PINK -> 3
                GoalTileType.BLUE -> 4
            }
        }
        goalTileList = sortedGoalTiles.toMutableList()


        // player buttons below the save button
        val playerButtonsY = saveButton.posY + saveButton.height + 20
        val playerButtonHeight = 35
        val playerButtonSpacing = 20

        // number of necessary player buttons
        val numberOfPlayerButtons = gameState.players.size

        // created player buttons
        for (i in 0 until numberOfPlayerButtons) {
            // current player needs no buttons
            val player = gameState.players[i]
            val playerButton = Button(
                posX = saveButton.posX,
                posY = playerButtonsY + (playerButtonHeight + playerButtonSpacing) * i,
                width = saveButton.width,
                height = playerButtonHeight,
                visual = ColorVisual(Color(0xffffff)).apply {
                    style.borderRadius = BorderRadius(20.0)
                },
                text = player.name,
                font = Font(30)
            ).apply {
                playerButtons.add(this)
                onMouseClicked = {
                    val game = rootService.currentGame?.currentBonsaiGameState
                    checkNotNull(game)
                    playerPanes.forEachIndexed { index, playerPane ->
                        val currentPlayerIndex = getOrder(game.currentPlayer)
                        if (index == i) {
                            playerPane.isVisible = true
                            if (index == currentPlayerIndex) {
                                showSupply(index)
                            }
                        } else {
                            playerPane.isVisible = false
                            hideSupply(index)
                        }
                    }
                }
            }
            buttonPane.add(playerButton)
        }

        val buttonHeight = 45
        val buttonSpacing = 30
        var currentY = 370

        // need 6 goal tiles for 2 players and 9 for 3
        val numberOfButtons = if (gameState.players.size == 2) 6 else 9

        // create goal tile buttons in correct order
        for (i in 0 until numberOfButtons) {
            val goalTile = sortedGoalTiles[i % sortedGoalTiles.size]
            val goalButton = Button(
                posX = 20,
                posY = currentY,
                width = 260,
                height = buttonHeight,
                visual = ColorVisual(Color(getColorForGoalTile(goalTile.goalTileType))).apply {
                    style.borderRadius = BorderRadius(20.0)
                },
                text = "Tier: ${goalTile.tier} Score: ${goalTile.score}",
                font = Font(30)
            )
            goalButtons.add(goalButton)
            buttonPane.add(goalButton)
            currentY += buttonHeight + buttonSpacing
        }

        // add panel to the scene
        addComponents(buttonPane)
    }

    private fun createPlayerPane(player: Player) {
        val playerPane =
            Pane<ComponentView>(
                posY = 380,
                width = 1920,
                height = 1080,
                visual = ColorVisual(Color(0, 0, 0, 0)),
            ).apply {
                val game = rootService.currentGame?.currentBonsaiGameState
                checkNotNull(game)
                isVisible = player == game.currentPlayer
            }
        playerPanes.add(playerPane)
        addComponents(playerPane)
    }

    // Overlay the game scene to prompt the player to choose tile
    private val overlayPane = Pane<ComponentView>(
        posX = 0, posY = 0,
        width = 1920, height = 1080,
    ).apply {
        isVisible = false
    }


    private val claimButton = Button(
        posX = 100,
        posY = 450,
        width = 250,
        height = 60,
        text = "Claim"
    )

    private val renounceButton = Button(
        posX = 400,
        posY = 450,
        width = 250,
        height = 60,
        text = "Renounce"
    )

    private val goalTileText = Label(
        posX = 175,
        posY = 100,
        width = 400,
        height = 50,
        text = "You can claim a goal tile"
    )

    // pane for claim or renounce goal tile
    private val goalTilePane =
        Pane<UIComponent>(
            posX = 585,
            posY = 240,
            width = 750,
            height = 600,
            visual = ColorVisual(Color(0xbebebe)).apply {
                style.borderRadius = BorderRadius(20.0)
            }
        ).apply {
            zIndex = 1
            isVisible = false
            isDisabled = true
            this.add(claimButton)
            this.add(renounceButton)
            this.add(goalTileText)

        }


    private val leafTile = HexagonView(
        posY = 100,
        posX = 500,
        visual = ColorVisual(Color(COLOUR_LEAF))
    )
    private val woodTile = HexagonView(
        posY = 100,
        posX = 150,
        visual = ColorVisual(Color(COLOUR_WOOD))
    )
    private val fruitTile = HexagonView(
        posY = 350,
        posX = 150,
        visual = ColorVisual(Color(COLOUR_FRUIT))
    )
    private val flowerTile = HexagonView(
        posY = 350,
        posX = 500,
        visual = ColorVisual(Color(COLOUR_FLOWER))
    )

    // pane for claim or renounce goal tile
    private val choseAnyTilePane =
        Pane<ComponentView>(
            posX = 585,
            posY = 240,
            width = 750,
            height = 600,
            visual = ColorVisual(Color(0xbebebe)).apply {
                style.borderRadius = BorderRadius(20.0)
            }
        ).apply {
            zIndex = 1
            isVisible = false
            isDisabled = true
            this.add(leafTile)
            this.add(woodTile)
            this.add(fruitTile)
            this.add(flowerTile)

        }

    init {
        addComponents(
            zenCardPane, infoPane, interactionPane, collectedCardPane,
            removeButton, cultivateButton, endTurnButton,
            zenDeckView, faceUpCards,
            overlayPane, goalTilePane, choseAnyTilePane
        )
    }

    //components for the tree boards

    private fun initPot(player: Player) {
        val playerPane = playerPanes[getOrder(player)]
        val treeHexagonGrid = HexagonGrid<HexagonView>(
            posX = 1000,
            posY = 1000,
            coordinateSystem = HexagonGrid.CoordinateSystem.AXIAL
        )
        treeHexagonGrids.add(treeHexagonGrid)

        val targetLayout = Pane<ComponentView>(
            width = 2000,
            height = 2000,
            visual = ColorVisual(Color(SECONDARY_COLOUR))
        ).apply {
            this.add(treeHexagonGrid)
        }
        targetLayouts.add(targetLayout)

        val treePane = CameraPane(
            posX = 240,
            posY = 0,
            width = 1320,
            height = 680,
            target = targetLayout,
            limitBounds = true
        ).apply {
            this.interactive = true
        }
        treePanes.add(treePane)
        playerPane.add(treePane)

        POT.forEach {
            var color = getColorForPot(player.color)
            if (it.second == 0) {
                color = 0x000000
            }
            if (it.first == 0 && it.second == 0) {
                color = COLOUR_WOOD
            }
            val hexagon = HexagonView(
                visual = CompoundVisual(
                    ColorVisual(Color(color)),
                    TextVisual(
                        text = "${it.first}, ${it.second}",
                        font = Font(10.0, Color(0x000000))
                    )
                ),
                size = 30
            )
            treeHexagonGrid[it.first, it.second] = hexagon
            if (it.first == 0 && it.second == 0) {
                val root = player.bonsaiTree[0 to 0]
                checkNotNull(root) { "not root" }
                treeTileMaps[getOrder(player)].add(
                    (root to hexagon)
                )
            }
        }
        // get the empty tiles
        createEmptyHex(player)
    }

    private fun initSupply(player: Player) {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        val playerPane = playerPanes[getOrder(player)]
        val supplyTileMap = supplyTileMaps[getOrder(player)]
        val woodSupplyDeck = Area<HexagonView>(
            posX = 60, posY = 30,
            width = 100, height = 100,
            visual = ColorVisual(Color(255, 255, 255, 0))
        )
        woodSupplyDecks.add(woodSupplyDeck)
        playerPane.add(woodSupplyDeck)
        // leaf supply deck
        val leafSupplyDeck = Area<HexagonView>(
            posX = 60, posY = 180,
            width = 100, height = 100,
            visual = ColorVisual(Color(255, 255, 255, 0))
        )
        leafSupplyDecks.add(leafSupplyDeck)
        playerPane.add(leafSupplyDeck)
        // flower supply deck
        val flowerSupplyDeck = Area<HexagonView>(
            posX = 60, posY = 330,
            width = 100, height = 100,
            visual = ColorVisual(Color(255, 255, 255, 0))
        )
        flowerSupplyDecks.add(flowerSupplyDeck)
        playerPane.add(flowerSupplyDeck)
        // fruit supply deck
        val fruitSupplyDeck = Area<HexagonView>(
            posX = 60, posY = 480,
            width = 100, height = 100,
            visual = ColorVisual(Color(255, 255, 255, 0))
        )
        fruitSupplyDecks.add(fruitSupplyDeck)
        playerPane.add(fruitSupplyDeck)

        player.personalSupply.forEachIndexed { index, it ->
            val supplyHex = HexagonView(
                visual = CompoundVisual(
                    ColorVisual(Color(getColorForTileType(it.tileType)))
                ),
                size = 60
            ).apply {
                this.isDraggable = true
                this.onDragGestureEnded = { _, success ->
                    if (success) {
                        this.isDraggable = false
                    }
                }
            }

            it.q = index
            if (supplyTileMap.containsForward(it)) {
                supplyTileMap.removeForward(it)
            }
            supplyTileMap.add(it to supplyHex)

            when (it.tileType) {
                TileType.WOOD -> {
                    woodSupplyDeck.add(supplyHex)
                }

                TileType.LEAF -> leafSupplyDeck.add(supplyHex)
                TileType.FLOWER -> flowerSupplyDeck.add(supplyHex)
                else -> fruitSupplyDeck.add(supplyHex)
            }
            hideSupply(getOrder(player))
        }
        updateSupplyAmount(player)
    }

    /**
     *
     */
    private fun updateSupply(player: Player) {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        val supplyTileMap = supplyTileMaps[getOrder(player)]
        val woodSupplyDeck = woodSupplyDecks[getOrder(player)]
        val leafSupplyDeck = leafSupplyDecks[getOrder(player)]
        val flowerSupplyDeck = flowerSupplyDecks[getOrder(player)]
        val fruitSupplyDeck = fruitSupplyDecks[getOrder(player)]
        supplyTileMap.clear()
        woodSupplyDeck.clear()
        leafSupplyDeck.clear()
        flowerSupplyDeck.clear()
        fruitSupplyDeck.clear()

        player.personalSupply.forEachIndexed { index, it ->
            val supplyHex = HexagonView(
                visual = CompoundVisual(
                    ColorVisual(Color(getColorForTileType(it.tileType)))
                ),
                size = 60
            ).apply {
                this.isDraggable = false
                this.onDragGestureEnded = { _, success ->
                    if (success) {
                        this.isDraggable = false
                    }
                }
            }

            it.q = index
            supplyTileMap.add(it to supplyHex)

            when (it.tileType) {
                TileType.WOOD -> woodSupplyDeck.add(supplyHex)
                TileType.LEAF -> leafSupplyDeck.add(supplyHex)
                TileType.FLOWER -> flowerSupplyDeck.add(supplyHex)
                else -> fruitSupplyDeck.add(supplyHex)
            }
        }
        updateSupplyAmount(player)
    }

    private fun initZenBoard() {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        game.zenDeck.forEach {
            val cardView = CardView(
                height = 160,
                width = 110,
                front = CompoundVisual(ColorVisual.WHITE, TextVisual("${it.id}")),
                back = ColorVisual.BLACK,
            )
            cardFrontSetter(it, cardView)
            cardView.showBack()
            zenDeckView.add(cardView)
            zenCardMap.add(it to cardView)
        }
        cardSumText.text = "${game.zenDeck.size}"

        game.faceUpCards.forEach {
            val cardView = CardView(
                height = 160,
                width = 110,
                front = CompoundVisual(ColorVisual.WHITE, TextVisual("${it.id}")),
                back = ColorVisual.BLACK,
            )
            cardFrontSetter(it, cardView)
            cardView.showFront()
            faceUpCards.add(cardView)
            zenCardMap.add(it to cardView)
        }
        applyCardPosition()
    }

    private fun updateZenBoard() {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        if (game.zenDeck.isNotEmpty()) {
            val newCardView = zenDeckView.pop()
            newCardView.showFront()
            val oldCardViews = faceUpCards.components.toMutableList()
            faceUpCards.clear()
            faceUpCards.add(newCardView)
            faceUpCards.addAll(oldCardViews)
            applyCardPosition()
            cardSumText.text = "${game.zenDeck.size}"
        }
        // TODO(pane needs to be smaller when less than four)
    }

    //refresher do something
    override fun refreshAfterGameStart() {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)

        //invisible buttons in network game
        val isLocalPlayer = game.currentPlayer.isLocal
        undoButton.isVisible = isLocalPlayer
        redoButton.isVisible = isLocalPlayer
        saveButton.isVisible = isLocalPlayer

        initZenBoard()
        game.players.forEach {
            createPlayerPane(it)
            treeTileMaps.add(BidirectionalMap())
            supplyTileMaps.add(BidirectionalMap())
            initPot(it)
            initSupply(it)
        }
        createRightSidePane()



        nameText.text = "Player: " + game.players[0].name
        updateSupply(game.players[0])
        showSupply(0)
        if (!rootService.treeService.canPlayWood()) {
            removeButton.isVisible = true
            interactionText.text = "Please select Cultivate/ Meditate/ Remove"
        } else {
            removeButton.isVisible = false
            interactionText.text = "Please select Cultivate/ Meditate"
        }
    }

    override fun refreshAfterReceivedTile(discard: Boolean) {
        if (discard) {
            interactionText.text = ""
            val game = rootService.currentGame?.currentBonsaiGameState
            checkNotNull(game)
            val playerIndex = getOrder(game.currentPlayer)
            val supplyTileMap = supplyTileMaps[playerIndex]
            val tobeRemoved: MutableList<Tile> = mutableListOf()
            // make the supply tiles draggable after cultivate start
            println("123")

            game.currentPlayer.personalSupply.forEach { _ ->
                val pane = Area<HexagonView>(
                    posX = 585,
                    posY = 240,
                    width = 750,
                    height = 600,
                    visual = ColorVisual(Color(0xbebebe)).apply {
                        style.borderRadius = BorderRadius(20.0)
                    }
                ).apply {
                    //zIndex = 1
                    isVisible = true

                    this.dropAcceptor = {dragEvent ->
                        when (dragEvent.draggedComponent) {
                            is HexagonView -> {
                                // If the card is valid, the card can be dropped and played
                                // some condition
                                val comp = dragEvent.draggedComponent as HexagonView
                                val tile = supplyTileMap.backward(comp)
                                true
                            }
                            else -> false
                        }

                    }

                    this.onDragDropped = { dragEvent ->
                        val comp = dragEvent.draggedComponent as HexagonView
                        val tile = supplyTileMap.backward(comp)
                        tobeRemoved.add(tile)
                    }
                    updateSupply(game.currentPlayer)
                }
            }
            rootService.playerActionService.discardSupplyTile(tobeRemoved)
        }


    }
    override fun refreshAfterDrawingHelperCard(){

        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        println(game.currentState)
        val playerIndex = getOrder(game.currentPlayer)
        val supplyTileMap = supplyTileMaps[playerIndex]
        updateSupply(game.currentPlayer)
        // make the supply tiles draggable after cultivate start
        game.players[playerIndex].personalSupply.forEach { supplyTile ->
           supplyTileMap[supplyTile].apply {
                isDraggable = true

            }
            }
        interactionText.text = "You may now place your tiles or end turn"
    }
    override fun refreshAfterDrawingMasterCardAny() {

        println("hello")
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)

        choseAnyTilePane.apply { isVisible = true }
        leafTile.apply {
            onMouseClicked = {
                rootService.playerActionService.chooseTile(TileType.LEAF)
                updateSupply(game.currentPlayer)
                interactionText.text = "You have received a leaf tile"
                game.currentPlayer.hasPlayed = true
                choseAnyTilePane.isVisible = false
            }
            woodTile.apply {
                onMouseClicked = {
                    rootService.playerActionService.chooseTile(TileType.WOOD)
                    updateSupply(game.currentPlayer)
                    choseAnyTilePane.isVisible = false
                    interactionText.text = "You have received a wood tile"
                    game.currentPlayer.hasPlayed = true
                }
            }
            fruitTile.apply {
                onMouseClicked = {
                    rootService.playerActionService.chooseTile(TileType.FRUIT)
                    updateSupply(game.currentPlayer)
                    choseAnyTilePane.isVisible = false
                    interactionText.text = "You have received a wood tile"
                    game.currentPlayer.hasPlayed = true
                }
            }
            flowerTile.apply {
                onMouseClicked = {
                    rootService.playerActionService.chooseTile(TileType.FLOWER)
                    updateSupply(game.currentPlayer)
                    choseAnyTilePane.isVisible = false
                    interactionText.text = "You have received a wood tile"
                    game.currentPlayer.hasPlayed = true
                }
            }

        }


    }

    override fun refreshAfterCultivateStart() {
        // refresh information telling the player to pick a tile
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        val playerIndex = getOrder(game.currentPlayer)
        val supplyTileMap = supplyTileMaps[playerIndex]

        // make the supply tiles draggable after cultivate start
        game.players[playerIndex].personalSupply.forEach { supplyTile ->
            supplyTileMap[supplyTile].apply {
                isDraggable = true
            }
        }
        interactionText.text = "You may now place your tiles or end turn"

    }

    override fun refreshAfterPlayTile(goalTileType: GoalTileType?, tier: Int) {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        if (goalTileType == null) {
            println(leafSupplyDecks[0].components.size)
        }

        if (goalTileType != null) {

            var goalTileScore = 0

            for (goalTile in goalTileList) {
                if (goalTile.goalTileType == goalTileType && goalTile.tier == tier) {
                    goalTileScore = goalTile.score
                }
            }
            //addComponents(goalTilePane)
            goalTilePane.add(
                Label(
                    posX = 125,
                    posY = 250,
                    width = 500,
                    height = 50,
                    visual = ColorVisual(Color(getColorForGoalTile(goalTileType))).apply {
                        style.borderRadius = BorderRadius(20.0)
                    },
                    text = "Goal Tile: $goalTileType Tier: $tier Score: $goalTileScore",
                    font = Font(30)
                )

            )
            goalTilePane.apply { this.isVisible = true }
            goalTilePane.apply { this.isDisabled = false }
            claimButton.apply {
                onMouseClicked = {
                    rootService.playerActionService.claimOrRenounceGoal(true, goalTileType, tier)
                    goalTilePane.isDisabled = true
                    goalTilePane.isVisible = false

                    goalTileList.forEach {
                        if (goalTileType == it.goalTileType && tier == it.tier) {
                            goalButtons[goalTileList.indexOf(it)].text =
                                game.currentPlayer.name + ", Score:  $goalTileScore"
                        }
                    }
                }

            }
            renounceButton.apply {
                onMouseClicked = {
                    rootService.playerActionService.claimOrRenounceGoal(false, goalTileType, tier)
                    goalTilePane.isDisabled = true
                    goalTilePane.isVisible = false
                }
            }


        }
    }

    override fun refreshAfterDiscardTile() {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        if(game.currentState != States.USING_HELPER){
            updateSupply(game.currentPlayer)
        }

    }
    override fun refreshAfterMeditate() {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        if(game.currentState != States.USING_HELPER){
            updateSupply(game.currentPlayer)
        }
    }

    override fun refreshAfterEndTurn() {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        val currentPlayerIndex = getOrder(game.currentPlayer)
        val lastPlayerIndex = when (currentPlayerIndex) {
            0 -> game.players.size - 1
            else -> currentPlayerIndex - 1
        }
        nameText.text = game.currentPlayer.name
        updateSupply(game.currentPlayer)
        showSupply(currentPlayerIndex)
        playerPanes[currentPlayerIndex].apply {
            isVisible = true
        }
        hideSupply(lastPlayerIndex)
        playerPanes[lastPlayerIndex].apply {
            isVisible = false
        }

        updateSupply(game.currentPlayer)

        if (!rootService.treeService.canPlayWood()) {
            removeButton.isVisible = true
            interactionText.text = "Please select Cultivate/ Meditate/ Remove"
        } else {
            removeButton.isVisible = false
            interactionText.text = "Please select Cultivate/ Meditate"
        }
    }


    private fun cardFrontSetter(generalCard: Card, cardView: CardView) {

        when (generalCard.cardType) {
            CardType.TOOLCARD -> {
                val card = generalCard as ToolCard
                cardView.apply {
                    frontVisual = CompoundVisual(
                        ColorVisual.WHITE,
                        TextVisual("${card.cardType}\n___2___")
                    )
                }
            }

            CardType.GROWTHCARD -> {
                val card = generalCard as GrowthCard
                cardView.apply {
                    frontVisual = CompoundVisual(
                        ColorVisual.WHITE,
                        TextVisual("${card.cardType}\n___${card.tileType}___")
                    )

                }
            }

            CardType.HELPERCARD -> {
                val card = generalCard as HelperCard
                cardView.apply {
                    frontVisual = CompoundVisual(
                        ColorVisual.WHITE,
                        TextVisual("${card.cardType}\n___${card.tileTypes[0]}___\n___${card.tileTypes[1]}___")
                    )
                }
            }

            CardType.MASTERCARD -> {
                val card = generalCard as MasterCard
                cardView.apply {
                    frontVisual = when (card.tileTypes.size) {
                        3 -> CompoundVisual(
                            ColorVisual.WHITE,
                            TextVisual(
                                "${card.cardType}\n___${card.tileTypes[0]}___\n" +
                                        "___${card.tileTypes[1]}___\n___${card.tileTypes[2]}___"
                            )
                        )

                        2 -> CompoundVisual(
                            ColorVisual.WHITE,
                            TextVisual("${card.cardType}\n___${card.tileTypes[0]}___\n___${card.tileTypes[1]}___")
                        )

                        else -> CompoundVisual(
                            ColorVisual.WHITE,
                            TextVisual("${card.cardType}\n___${card.tileTypes[0]}___")
                        )

                    }
                }
            }

            CardType.PARCHMENTCARD -> {
                val card = generalCard as ParchmentCard
                cardView.apply {
                    frontVisual = if (card.parchmentCardType != null) {
                        CompoundVisual(
                            ColorVisual.WHITE,
                            TextVisual("${card.cardType}\n___${card.parchmentCardType}___\n___${card.basePoints}___")
                        )
                    } else {
                        CompoundVisual(
                            ColorVisual.WHITE,
                            TextVisual("${card.cardType}\n___${card.parchmentTileType}___\n___${card.basePoints}___")
                        )
                    }
                }
            }
        }
    }

    private fun applyCardPosition() {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        faceUpCards.forEachIndexed { index, card ->

            when (index) {
                0 -> {
                    card.apply {

                        onMouseClicked = {
                            if (game.currentState == States.START_TURN ||
                                game.currentState == States.CHOOSE_ACTION ||
                                game.currentState == States.REMOVE_TILES
                            ) {
                                interactionText.text = " no extra tiles "
                                rootService.playerActionService.meditate(0, null)
                                removeFromParent()
                                // updateSupply(game.currentPlayer)
                                updateZenBoard()
                            }
                        }
                    }
                }

                1 -> {
                    card.apply {
                        onMouseClicked = {
                            if (game.currentState == States.START_TURN ||
                                game.currentState == States.CHOOSE_ACTION ||
                                game.currentState == States.REMOVE_TILES
                            ) {
                                interactionText.text = "Choose tile to claim: "

                                overlayPane.clear()
                                overlayPane.isVisible = true

                                overlayPane.add(
                                    HexagonView(
                                        posX = 1260, posY = 270,
                                        size = 40,
                                        visual = ColorVisual(Color(COLOUR_LEAF))
                                    )
                                        .apply {
                                            onMouseClicked = {
                                                rootService.playerActionService.meditate(1, TileType.LEAF)
                                              //  updateSupply(game.currentPlayer)
                                                overlayPane.isVisible = false
                                                interactionText.text = "You have received a leaf tile"

                                            }
                                        })
                                overlayPane.add(
                                    HexagonView(
                                        posX = 1380, posY = 270,
                                        size = 40,
                                        visual = ColorVisual(Color(COLOUR_WOOD))
                                    ).apply {
                                        onMouseClicked = {
                                            rootService.playerActionService.meditate(1, TileType.WOOD)
                                            // updateSupply(game.currentPlayer)
                                            overlayPane.isVisible = false
                                            interactionText.text = "You have received a wood tile"

                                        }
                                    })

                                removeFromParent()
                               //  updateSupply(game.currentPlayer)
                                updateZenBoard()
                            }
                        }
                    }
                }

                2 -> {
                    card.apply {
                        onMouseClicked = {
                            if (game.currentState == States.START_TURN ||
                                game.currentState == States.CHOOSE_ACTION ||
                                game.currentState == States.REMOVE_TILES
                            ) {
                                interactionText.text = "You have received a wood and a flower tile"
                                rootService.playerActionService.meditate(2, null)
                                removeFromParent()
                                //updateSupply(game.currentPlayer)
                                updateZenBoard()
                            }
                        }
                    }
                }


                else -> {
                    card.apply {
                        onMouseClicked = {
                            if (game.currentState == States.START_TURN ||
                                game.currentState == States.CHOOSE_ACTION ||
                                game.currentState == States.REMOVE_TILES
                            ) {
                                interactionText.text = "You have received a leaf and a fruit tile"
                                rootService.playerActionService.meditate(3, null)
                                removeFromParent()
                                // updateSupply(game.currentPlayer)
                                updateZenBoard()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun switchPlayerPane(playerIndex: Int, hasEndTurn: Boolean) {

    }

    /**
     * really important function for play tile
     */
    private fun createEmptyHex(player: Player) {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        val treeTileMap = treeTileMaps[getOrder(player)]
        val supplyTileMap = supplyTileMaps[getOrder(player)]
        val treeHexagonGrid = treeHexagonGrids[getOrder(player)]

        player.bonsaiTree.getEmptyTiles().forEach {
            //println("${it.first}, ${it.second}")
            val hexagon = HexagonView(
                visual = CompoundVisual(
                    ColorVisual(255, 255, 255, 0.3),
                    TextVisual(
                        text = "${it.first}, ${it.second}",
                        font = Font(10.0, Color(0x000000))
                    )
                ),
                size = 30
            ).apply {
                val emptyTile = Tile(it.first, it.second, TileType.EMPTY)
                if (treeTileMap.contains(emptyTile to this)) {
                    treeTileMap.remove(emptyTile to this)
                    treeTileMap.add(emptyTile to this)
                } else {
                    treeTileMap.add(emptyTile to this)
                }


                this.dropAcceptor = { dragEvent ->
                    when (dragEvent.draggedComponent) {
                        is HexagonView -> {
                            // If the card is valid, the card can be dropped and played
                            // some condition
                            val comp = dragEvent.draggedComponent as HexagonView
                            val tile = supplyTileMap.backward(comp)
                            rootService.treeService.canPlayTile(tile, it)
                        }

                        else -> false
                    }
                }

                this.onDragDropped = { dragEvent ->
                    val comp = dragEvent.draggedComponent as HexagonView
                    val tile = supplyTileMap.backward(comp)
                    tile.q = it.first
                    tile.r = it.second
                    treeTileMap.remove(emptyTile to this)
                    treeTileMap.add(tile to this)
                    visual = CompoundVisual(
                        ColorVisual(Color(getColorForTileType(tile.tileType))),
                        TextVisual(
                            text = "${it.first}, ${it.second}",
                            font = Font(10.0, Color(0x000000))
                        )
                    )
                    rootService.treeService.playTile(tile, it)
                    val parentDeck = comp.parent //as Area<HexagonView>
                    check(parentDeck is Area<*>)

                    comp.removeFromParent()
                    if (parentDeck.components.isNotEmpty()) {
                        val lastHex = parentDeck.last()
                        check(lastHex is HexagonView)
                        val supplyTileType = supplyTileMap.backward(lastHex).tileType
                        parentDeck.last().apply {
                            this.visual = CompoundVisual(
                                ColorVisual(Color(getColorForTileType(supplyTileType))),
                                TextVisual(
                                    "${parentDeck.components.size}",
                                    font = Font(30.0, Color(0x000000))
                                )
                            )
                        }
                    }
                    createEmptyHex(player)
                }
            }
            treeHexagonGrid[it.first, it.second] = hexagon
        }

    }

    private fun updateSupplyAmount(player: Player) {

        if (woodSupplyDecks[getOrder(player)].components.isNotEmpty()) {
            woodSupplyDecks[getOrder(player)].components.last().apply {
                this.visual = CompoundVisual(
                    ColorVisual(Color(COLOUR_WOOD)),
                    TextVisual(
                        "${woodSupplyDecks[getOrder(player)].components.size}",
                        font = Font(30.0, Color(0x000000))
                    )
                )
            }
        }

        if (leafSupplyDecks[getOrder(player)].components.isNotEmpty()) {
            leafSupplyDecks[getOrder(player)].components.last().apply {
                this.visual = CompoundVisual(
                    ColorVisual(Color(COLOUR_LEAF)),
                    TextVisual(
                        "${leafSupplyDecks[getOrder(player)].components.size}",
                        font = Font(30.0, Color(0x000000))
                    )
                )
            }
        }

        if (flowerSupplyDecks[getOrder(player)].components.isNotEmpty()) {
            flowerSupplyDecks[getOrder(player)].components.last().apply {
                this.visual = CompoundVisual(
                    ColorVisual(Color(COLOUR_FLOWER)),
                    TextVisual(
                        "${flowerSupplyDecks[getOrder(player)].components.size}",
                        font = Font(30.0, Color(0x000000))
                    )
                )
            }
        }

        if (fruitSupplyDecks[getOrder(player)].components.isNotEmpty()) {
            fruitSupplyDecks[getOrder(player)].components.last().apply {
                this.visual = CompoundVisual(
                    ColorVisual(Color(COLOUR_FRUIT)),
                    TextVisual(
                        "${fruitSupplyDecks[getOrder(player)].components.size}",
                        font = Font(30.0, Color(0x000000))
                    )
                )
            }
        }
    }

    private fun showSupply(index: Int) {
        woodSupplyDecks[index].isVisible = true
        leafSupplyDecks[index].isVisible = true
        flowerSupplyDecks[index].isVisible = true
        fruitSupplyDecks[index].isVisible = true
    }

    private fun hideSupply(index: Int) {
        woodSupplyDecks[index].isVisible = false
        leafSupplyDecks[index].isVisible = false
        flowerSupplyDecks[index].isVisible = false
        fruitSupplyDecks[index].isVisible = false
    }

    private fun getColorForGoalTile(goalTileType: GoalTileType): Int {
        return when (goalTileType) {
            GoalTileType.BROWN -> COLOUR_WOOD
            GoalTileType.GREEN -> COLOUR_LEAF
            GoalTileType.ORANGE -> COLOUR_FRUIT
            GoalTileType.PINK -> COLOUR_FLOWER
            GoalTileType.BLUE -> COLOUR_BLUE
        }
    }

    private fun getColorForTileType(tileType: TileType): Int {
        return when (tileType) {
            TileType.WOOD -> COLOUR_WOOD
            TileType.LEAF -> COLOUR_LEAF
            TileType.FLOWER -> COLOUR_FLOWER
            else -> COLOUR_FRUIT
        }
    }

    private fun getColorForPot(color: ColorType): Int {
        return when (color) {
            ColorType.RED -> COLOUR_RED
            ColorType.PURPLE -> COLOUR_PURPLE
            ColorType.BLACK -> COLOUR_BLACK
            ColorType.BLUE -> COLOUR_BLUE
        }
    }

    private fun getOrder(player: Player): Int {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        game.players.forEachIndexed { index, playerInList ->
            if (player == playerInList) {
                return index
            }
        }
        return -1
    }

}
