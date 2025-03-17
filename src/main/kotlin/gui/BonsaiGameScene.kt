package gui

import entity.*
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
    BoardGameScene(1920,1080, ColorVisual(Color(PRIMARY_COLOUR))) , Refreshable {

    private val treeTileMaps: MutableList<BidirectionalMap<Tile, HexagonView>> = mutableListOf()
    private val supplyTileMaps: MutableList<BidirectionalMap<Tile, HexagonView>> = mutableListOf()
    private val zenCardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    private val playerPanes: MutableList<Pane<UIComponent>> = mutableListOf()
    private val treeHexagonGrids: MutableList<HexagonGrid<HexagonView>> = mutableListOf()
    private val targetLayouts: MutableList<Pane<ComponentView>> = mutableListOf()
    private val treePanes: MutableList<ComponentView> = mutableListOf()
    private val woodSupplyDecks: MutableList<Area<HexagonView>> = mutableListOf()
    private val leafSupplyDecks: MutableList<Area<HexagonView>> = mutableListOf()
    private val flowerSupplyDecks: MutableList<Area<HexagonView>> = mutableListOf()
    private val fruitSupplyDecks: MutableList<Area<HexagonView>> = mutableListOf()


    // button for cultivate
    private val cultivateButton =
        Button(
            posX = 360,
            posY = 270,
            width = 150,
            height = 80,
            visual = ColorVisual(Color(0xffffff)).apply {
                style.borderRadius = BorderRadius(20.0)
            },
            text = "cultivate",
            font = Font(36)
        ).apply {
            onMouseClicked = {
                rootService.playerActionService.cultivate()
            }
        }

    // button for meditate
    private val meditateButton =
        Button(
            posX = 190,
            posY = 270,
            width = 150,
            height = 80,
            visual = ColorVisual(Color(0xffffff)).apply {
                style.borderRadius = BorderRadius(20.0)
            },
            text = "meditate",
            font = Font(36)
        )

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

    // button for end turn
    private val endTurnButton =
        Button(
            posX = 1400,
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
                rootService.playerActionService.endTurn()
            }
        }

    // pane for the cards
    private val zenCardPane =
        Pane<UIComponent>(
            posX = 20,
            posY = 20,
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
        width = 800,
        height = 60,
        text = "",
        font = Font(40, Color(TERTIARY_COLOUR))
    )

    // pane for the interaction
    private val interactionPane =
        Pane<ComponentView>(
            posX = 540,
            posY = 260,
            width = 840,
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
            posX = 20, // Adjusted to fit within the buttonPane
            posY = 30, // Adjusted to fit within the buttonPane
            width = 50,
            height = 40,
            visual = ColorVisual(Color(0xffffff)).apply {
                style.borderRadius = BorderRadius(20.0)
            },
            text = "⬅", // Thick left arrow Unicode symbol
            font = Font(36) // Adjust font size as needed
        )

    private val redoButton =
        Button(
            posX = 230, // Adjusted to fit within the buttonPane
            posY = 30, // Adjusted to fit within the buttonPane
            width = 50,
            height = 40,
            visual = ColorVisual(Color(0xffffff)).apply {
                style.borderRadius = BorderRadius(20.0)
            },
            text = "➡", // Thick right arrow Unicode symbol
            font = Font(36) // Adjust font size as needed
        )

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
        )


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
        }.map { it.goalTileType }

        // right side pane
        val buttonPane = Pane<UIComponent>(
            posX = 1600,
            posY = 20,
            width = 300,
            height = 1040,
            visual = ColorVisual(Color(SECONDARY_COLOUR)).apply {
                style.borderRadius = BorderRadius(20.0)
            }
        )

        // undo, redo, and save buttons
        buttonPane.add(undoButton)
        buttonPane.add(redoButton)
        buttonPane.add(saveButton)

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
                font = Font(20)
            )
            buttonPane.add(playerButton)
        }

        val buttonHeight = 45
        val buttonSpacing = 30
        var currentY = 370

        // need 6 goal tiles for 2 players and 9 for 3
        val numberOfButtons = if (gameState.players.size == 2) 6 else 9

        // create goal tile buttons in correct order
        for (i in 0 until numberOfButtons) {
            val goalTileType = sortedGoalTiles[i % sortedGoalTiles.size]
            val button = Button(
                posX = 20,
                posY = currentY,
                width = 260,
                height = buttonHeight,
                visual = ColorVisual(Color(getColorForGoalTile(goalTileType))).apply {
                    style.borderRadius = BorderRadius(20.0)
                },
                text = goalTileType.name,
                font = Font(20)
            )

            buttonPane.add(button)
            currentY += buttonHeight + buttonSpacing
        }

        // add panel to the scene
        addComponents(buttonPane)
    }


    /* Create three rings of hexagons */


    //initialize cardviews
    //map the cardviews to cards

    init {
        addComponents(
            zenCardPane, infoPane, interactionPane, collectedCardPane,
            removeButton, meditateButton, cultivateButton, endTurnButton,
            zenDeckView, faceUpCards,
        )
    }

    //components for the tree boards

    private fun initPot(player: Player) {
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
            //visual = ImageVisual("background.png")
        ).apply {
            this.add(treeHexagonGrid)
        }

        val treePane = CameraPane(
            posX = 240,
            posY = 380,
            width = 1320,
            height = 680,
            target = targetLayout,
            limitBounds = true
        ).apply {
            this.interactive = true
        }
        treePanes.add(treePane)

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
        }

        //map the tiles to hexagon
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        treeTileMaps[getOrder(player)].add(
            (player.bonsaiTree[0 to 0] to treeHexagonGrid[0, 0]) as Pair<Tile, HexagonView>
        )

        // get the empty tiles
        createEmptyHex(player)
    }

    private fun initSupply(player: Player) {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        val supplyTileMap = supplyTileMaps[getOrder(player)]
        val woodSupplyDeck = Area<HexagonView>(
            posX = 60, posY = 410,
            width = 100, height = 100,
            visual = ColorVisual(Color(255, 255, 255, 0))
        )
        woodSupplyDecks.add(woodSupplyDeck)
        // leaf supply deck
        val leafSupplyDeck = Area<HexagonView>(
            posX = 60, posY = 560,
            width = 100, height = 100,
            visual = ColorVisual(Color(255, 255, 255, 0))
        )
        leafSupplyDecks.add(leafSupplyDeck)
        // flower supply deck
        val flowerSupplyDeck = Area<HexagonView>(
            posX = 60, posY = 710,
            width = 100, height = 100,
            visual = ColorVisual(Color(255, 255, 255, 0))
        )
        flowerSupplyDecks.add(flowerSupplyDeck)
        // fruit supply deck
        val fruitSupplyDeck = Area<HexagonView>(
            posX = 60, posY = 860,
            width = 100, height = 100,
            visual = ColorVisual(Color(255, 255, 255, 0))
        )
        fruitSupplyDecks.add(fruitSupplyDeck)

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
                TileType.WOOD -> woodSupplyDeck.add(supplyHex)
                TileType.LEAF -> leafSupplyDeck.add(supplyHex)
                TileType.FLOWER -> flowerSupplyDeck.add(supplyHex)
                else -> fruitSupplyDeck.add(supplyHex)
            }
        }
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

    //refresher do something
    override fun refreshAfterGameStart() {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        initZenBoard()
        createRightSidePane()
        game.players.forEach {
            treeTileMaps.add(BidirectionalMap())
            supplyTileMaps.add(BidirectionalMap())
            initPot(it)
            initSupply(it)
        }

        addComponents(treePanes[0], woodSupplyDecks[0], leafSupplyDecks[0],
            flowerSupplyDecks[0], fruitSupplyDecks[0])



        interactionText.text = "Please select Cultivate or Meditate"
        nameText.text = "Player: " + game.players[0].name
        updateSupplyAmount(game.players[0])
    }

    override fun refreshAfterCultivateStart() {
        // refresh information telling the player to pick a tile
        interactionText.text = "You may now place your tiles"
    }

    override fun refreshAfterPlayTile(goalTileType: GoalTileType?, tier: Int) {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        if (goalTileType == null) {
            println(leafSupplyDecks[0].components.size)
        }
    }

    override fun refreshAfterEndTurn() {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        nameText.text = game.currentPlayer.name
        updateSupplyAmount(game.currentPlayer)
    }


    private fun cardFrontSetter(generalCard: Card, cardView: CardView) {
        when (generalCard.cardType) {
            CardType.TOOLCARD -> {
                val card = generalCard as ToolCard
                cardView.apply {
                    frontVisual = CompoundVisual(
                        ColorVisual.WHITE,
                        TextVisual("${card.cardType}\n" + "___2___")
                    )
                }
            }

            CardType.GROWTHCARD -> {
                val card = generalCard as GrowthCard
                cardView.apply {
                    frontVisual = CompoundVisual(
                        ColorVisual.WHITE,
                        TextVisual("${card.cardType}\n" + "___${card.tileType}___")
                    )

                }
            }

            CardType.HELPERCARD -> {
                val card = generalCard as HelperCard
                cardView.apply {
                    frontVisual = CompoundVisual(
                        ColorVisual.WHITE,
                        TextVisual("${card.cardType}\n" + "___${card.tileTypes[0]}___\n" + "___${card.tileTypes[1]}___")
                    )
                }
            }

            CardType.MASTERCARD -> {
                val card = generalCard as MasterCard
                cardView.apply {
                    frontVisual = when (card.tileTypes.size) {
                        3 -> CompoundVisual(
                            ColorVisual.WHITE,
                            TextVisual("${card.cardType}\n" + "___${card.tileTypes[0]}___\n" + "___${card.tileTypes[1]}___\n" + "___${card.tileTypes[2]}___")
                        )

                        2 -> CompoundVisual(
                            ColorVisual.WHITE,
                            TextVisual("${card.cardType}\n" + "___${card.tileTypes[0]}___\n" + "___${card.tileTypes[1]}___")
                        )

                        else -> CompoundVisual(
                            ColorVisual.WHITE,
                            TextVisual("${card.cardType}\n" + "___${card.tileTypes[0]}___")
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

    // TODO(it's not working)
    private fun applyCardPosition() {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        faceUpCards.forEachIndexed {  index, card ->
            when (index) {
                3 -> {
                    card.apply {
                        // TODO(need to make a choose pane)
                        onMouseClicked = {
                            rootService.playerActionService.meditate(3, TileType.LEAF)
                        }
                    }
                }

                2 -> {
                    card.apply {
                        onMouseClicked = {
                            rootService.playerActionService.meditate(2, null)
                        }
                    }
                }

                1 -> {
                    card.apply {
                        onMouseClicked = {
                            rootService.playerActionService.meditate(1, null)
                        }
                    }
                }

                else -> {} }
            /**
        game.faceUpCards.forEachIndexed { index, card ->
            when (index) {
                2 -> {
                    zenCardMap.forward(card).apply {
                        // TODO(need to make a choose pane)
                        onMouseClicked = {
                            rootService.playerActionService.meditate(1, TileType.LEAF)
                        }
                    }
                }

                1 -> {
                    zenCardMap.forward(card).apply {
                        onMouseClicked = {
                            rootService.playerActionService.meditate(2, null)
                        }
                    }
                }

                0 -> {
                    zenCardMap.forward(card).apply {
                        onMouseClicked = {
                            rootService.playerActionService.meditate(3, null)
                        }
                    }
                }

                else -> {}
            }
        */
        }
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
                println("${it.first}, ${it.second}")
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
                    if (treeTileMap.contains(emptyTile to this)){
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
                        val parentDeck = comp.parent as Area<HexagonView>
                        comp.removeFromParent()
                        if (parentDeck.components.isNotEmpty()){
                            val supplyTileType = supplyTileMap.backward(parentDeck.last()).tileType
                            parentDeck.last().apply {
                                this.visual = CompoundVisual(ColorVisual(Color(getColorForTileType(supplyTileType))),
                                    TextVisual("${parentDeck.components.size}",
                                        font = Font(30.0, Color(0x000000))))
                            }
                        }
                        createEmptyHex(player)
                    }
                }
                treeHexagonGrid[it.first, it.second] = hexagon
            }

    }

    // TODO(also not working)
    private fun updateSupplyAmount(player: Player) {

        if (woodSupplyDecks[getOrder(player)].components.isNotEmpty()){
            woodSupplyDecks[getOrder(player)].components.last().apply {
                this.visual = CompoundVisual(ColorVisual(Color(COLOUR_WOOD)),
                    TextVisual("${woodSupplyDecks[getOrder(player)].components.size}",
                        font = Font(30.0, Color(0x000000))))
            }
        }

        if (leafSupplyDecks[getOrder(player)].components.isNotEmpty()){
            leafSupplyDecks[getOrder(player)].components.last().apply {
                this.visual = CompoundVisual(ColorVisual(Color(COLOUR_LEAF)),
                    TextVisual("${leafSupplyDecks[getOrder(player)].components.size}",
                        font = Font(30.0, Color(0x000000))))
            }
        }

        if (flowerSupplyDecks[getOrder(player)].components.isNotEmpty()){
            flowerSupplyDecks[getOrder(player)].components.last().apply {
                this.visual = CompoundVisual(ColorVisual(Color(COLOUR_FLOWER)),
                    TextVisual("${flowerSupplyDecks[getOrder(player)].components.size}",
                        font = Font(30.0, Color(0x000000))))
            }
        }

        if (fruitSupplyDecks[getOrder(player)].components.isNotEmpty()){
            fruitSupplyDecks[getOrder(player)].components.last().apply {
                this.visual = CompoundVisual(ColorVisual(Color(COLOUR_FRUIT)),
                    TextVisual("${fruitSupplyDecks[getOrder(player)].components.size}",
                        font = Font(30.0, Color(0x000000))))
            }
        }
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
