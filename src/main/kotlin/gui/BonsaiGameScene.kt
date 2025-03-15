package gui

import entity.*
import service.RootService
import tools.aqua.bgw.components.ComponentView
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
import tools.aqua.bgw.core.Alignment
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

    private val treeTileMap: BidirectionalMap<Tile, HexagonView> = BidirectionalMap()
    private val supplyTileMap: BidirectionalMap<Tile, HexagonView> = BidirectionalMap()
    private val zenCardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()
    private val clickedSupply : HexagonView? = null

    // button for cultivate
    private val cultivateButton =
        Button(
            posX = 380,
            posY = 270,
            width = 160,
            height = 80,
            visual = ColorVisual(Color(0xffffff)).apply {
                style.borderRadius = BorderRadius(20.0)
            },
            text = "cultivate",
            font = Font(36)
        ).apply {
            onMouseClicked= {
                rootService.playerActionService.cultivate()
            }
        }

    // button for meditate
    private val meditateButton =
        Button(
            posX = 200,
            posY = 270,
            width = 160,
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
            width = 160,
            height = 80,
            visual = ColorVisual(Color(0xffffff)).apply {
                style.borderRadius = BorderRadius(20.0)
            },
            text = "remove",
            font = Font(36)
        )

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

    // pane for player info
    private val infoPane =
        Pane<UIComponent>(
            posX = 737,
            posY = 20,
            width = 400,
            height = 220,
            visual = ColorVisual(Color(SECONDARY_COLOUR)).apply {
                style.borderRadius = BorderRadius(20.0)
            }
        )

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

    // pane for the interaction
    private val interactionPane =
        Pane<UIComponent>(
            posX = 560,
            posY = 260,
            width = 1000,
            height = 100,
            visual = ColorVisual(Color(SECONDARY_COLOUR)).apply {
                style.borderRadius = BorderRadius(20.0)
            }
        )


    //components for the zenCards area
    //components for the personal information
    //components for the discardTree, meditate, cultivate buttons
    //components for the interaction dialogue area
    //components for the redo and undo area
    //components for other players and its buttons
    //components for goal tiles

    //camera pane

    private val panLabel = Label(
        posX = 300,
        posY = 410,
        width = 400,
        height = 200,
        text = "Drag to pan the camera. Scroll to zoom.",
        alignment = Alignment.CENTER,
        font = Font(20.0, Color.WHITE)
    )

    private val targetLayout = Pane<ComponentView>(
        width = 2000,
        height = 2000,
        visual = ColorVisual(Color(SECONDARY_COLOUR))
        //visual = ImageVisual("background.png")
    ).apply {
        this.add(panLabel)
    }


    // components for the tree
    private val treeHexagonGrid = HexagonGrid<HexagonView>(
        posX = 1000,
        posY = 1000,
        coordinateSystem = HexagonGrid.CoordinateSystem.AXIAL
    )

    private val treePane = CameraPane(
        posX = 240,
        posY = 380,
        width = 1320,
        height = 680,
        target = targetLayout,
        limitBounds = true
    ).apply {
        this.interactive = true
    }

    //components for the supplies
    private val woodSupply = HexagonView(
        posX = 60,
        posY = 410,
        visual = CompoundVisual(
            ColorVisual(Color(COLOUR_WOOD)),
            TextVisual(
                text = "5",
                font = Font(30.0, Color(0x000000))
            )
        ),
        size = 60
    )

    private val leafSupply = HexagonView(
        posX = 60,
        posY = 560,
        visual = CompoundVisual(
            ColorVisual(Color(COLOUR_LEAF)),
            TextVisual(
                text = "5",
                font = Font(30.0, Color(0x000000))
            )
        ),
        size = 60
    ).apply {
        // TODO()
    }

    private val flowerSupply = HexagonView(
        posX = 60,
        posY = 710,
        visual = CompoundVisual(
            ColorVisual(Color(COLOUR_FLOWER)),
            TextVisual(
                text = "5",
                font = Font(30.0, Color(0x000000))
            )
        ),
        size = 60
    )

    private val fruitSupply = HexagonView(
        posX = 60,
        posY = 860,
        visual = CompoundVisual(
            ColorVisual(Color(COLOUR_FRUIT)),
            TextVisual(
                text = "5",
                font = Font(30.0, Color(0x000000))
            )
        ),
        size = 60
    )

    //zenDeck
    private val zenDeckView = CardStack<CardView>(
        posX = 60, posY = 40,
        width = 110, height = 160)

    //faceup cards
    private val faceUpCards = LinearLayout<CardView>(
        posX = 205,
        posY = 40,
        width = 500,
        height = 160,
        spacing = 13
    )




    /* Create three rings of hexagons */



    //initialize cardviews
    //map the cardviews to cards

    init {
        addComponents(leafSupply, woodSupply, flowerSupply, fruitSupply,
            zenCardPane, infoPane, interactionPane, collectedCardPane,
            removeButton, meditateButton, cultivateButton,
            zenDeckView, faceUpCards)
    }

    //components for the tree boards
    //initialize board
    /**
    //create hexagon
    fun initHexagon() {
        for (row in -8..8) {
            for (col in -8..8) {
                /* Only add hexagons that would fit in a circle */
                if(row + col in -8..8) {
                    val hexagon = HexagonView(
                        visual = CompoundVisual(
                            ColorVisual(Color(0xc6ff6e)),
                            TextVisual(
                                text = "$col, $row",
                                font = Font(10.0, Color(0x0f141f))
                            )
                        ),
                        size = 30
                    )
                    treeHexagonGrid[col, row] = hexagon
                }
            }
        }
    }
    */

    private fun initPot() {
        POT.forEach{
            var color = COLOUR_RED
            if (it.second == 0) {
                color = 0x000000
            }
            if (it.first == 0 && it.second == 0){
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
        treeTileMap.add((game.currentPlayer.bonsaiTree[0 to 0] to treeHexagonGrid[0,0]) as Pair<Tile, HexagonView>)

        // get the empty tiles
        game.currentPlayer.bonsaiTree.getEmptyTiles().forEach{
            val hexagon = HexagonView(
                visual = CompoundVisual(
                    ColorVisual(Color.WHITE),
                    TextVisual(
                        text = "${it.first}, ${it.second}",
                        font = Font(10.0, Color(0x000000))
                    )
                ),
                size = 30
            )
            treeHexagonGrid[it.first, it.second] = hexagon
        }
    }

    private fun initZenBoard() {
        val game = rootService.currentGame?.currentBonsaiGameState
        checkNotNull(game)
        game.zenDeck.forEach {
            val cardView = CardView(
                height = 160,
                width = 110,
                front = CompoundVisual(ColorVisual.WHITE),
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
                front  = ColorVisual.WHITE,
                back = ColorVisual.BLACK,
            )
            cardFrontSetter(it, cardView)
            cardView.showFront()
            faceUpCards.add(cardView)
            zenCardMap.add(it to cardView)
        }
    }

    //refresher do something
    override fun refreshAfterGameStart() {
        initPot()
        targetLayout.apply {
            this.add(treeHexagonGrid)
        }
        addComponents(treePane)
        initZenBoard()
    }

    override fun refreshAfterCultivateStart() {
        // refresh information telling the player to pick a tile
    }

    private fun cardFrontSetter(generalCard : Card, cardView : CardView) {
        when (generalCard.cardType){
            CardType.TOOLCARD -> {
                val card = generalCard as ToolCard
                CompoundVisual(ColorVisual.WHITE,
                    TextVisual("${card.cardType}\n" + "___2___"))
            }
            CardType.GROWTHCARD -> {
                val card = generalCard as GrowthCard
                cardView.apply {
                    card.apply {
                        frontVisual = CompoundVisual(ColorVisual.WHITE,
                            TextVisual("${card.cardType}\n" +"___${card.tileType}___"))
                    }
                }
            }
            CardType.HELPERCARD -> {
                val card = generalCard as HelperCard
                cardView.apply {
                    card.apply {
                        frontVisual = CompoundVisual(ColorVisual.WHITE,
                            TextVisual("${card.cardType}\n" + "___${card.tileTypes[0]}___\n" + "___${card.tileTypes[1]}___",
                                font = Font(15)))
                    }
                }
            }
            CardType.MASTERCARD -> {
                val card = generalCard as MasterCard
                cardView.apply {
                    frontVisual = when (card.tileTypes.size) {
                        3 -> CompoundVisual(
                            ColorVisual.WHITE,
                            TextVisual("${card.cardType}\n___${card.tileTypes[0]}___\n___${card.tileTypes[1]}___\n___${card.tileTypes[2]}___")
                        )

                        2 -> CompoundVisual(
                            ColorVisual.WHITE,
                            TextVisual(
                                "${card.cardType}\n" + "___${card.tileTypes[0]}___\n" + "___${card.tileTypes[1]}___",
                                font = Font(15)
                            )
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
                        CompoundVisual(ColorVisual.WHITE,
                            TextVisual("${card.cardType}\n___${card.parchmentCardType}___\n___${card.basePoints}___"))
                    } else {
                        CompoundVisual(ColorVisual.WHITE,
                            TextVisual("${card.cardType}\n___${card.parchmentTileType}___\n___${card.basePoints}___"))
                    }
                }
            }
        }
    }
}
