package gui

import entity.Player
import service.RootService
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

/**
 * The [ResultScene] is a [MenuScene] to show the player the result of a game of bonsai
 * and lets the user return to the [MainMenuScene] or to quit the application
 *
 */
class ResultScene(
    private val bonsaiApplication: BonsaiApplication,
    private val rootService: RootService
) :
    MenuScene(1920, 1080, ImageVisual("Backgrounds/Hintergrund2.png", 1920, 1080)), Refreshable {

    // Display scene name
    private val titleLabel = Label(
        0,
        0,
        800,
        210,
        "RESULT",
        font = Font(164.0, Color(0x000000), "Arial Black", Font.FontWeight.BOLD),
    )

    // Display winner
    private val resultLabel = Label(
        text = "Winner",
        width = 680, height = 130, posX = 1060, posY = 850,
        alignment = Alignment.CENTER,
        font = Font(size = 70)
    )

    // Button to quit the game
    private val quitButton = Button(
        posX = 80,
        posY = 900,
        width = 280,
        height = 110,
        text = "QUIT",
    ).apply {
        bonsaiApplication.exit()
    }

    // Button to back to menu scene
    private val backToMenuButton = Button(
        posX = 460,
        posY = 900,
        width = 280,
        height = 110,
        text = "MENU",
    ).apply {
        onMouseClicked = {
            bonsaiApplication.showMainMenuScene()
        }
    }

    // Show score board
    private val scoreImg =
        Pane<UIComponent>(
            posX = 990,
            posY = 190,
            width = 870,
            height = 630,
            visual = ImageVisual("ScoreBoard.png").apply {
                style.borderRadius = BorderRadius(20.0)
            }
        )

    private val scoreBoard = TableView<PlayerScore>(
        posX = 990,
        posY = 260,
        width = 870,
        height = 558,
        columns = listOf(
            // Column for Player Names
            TableColumn(
                title = "Name",
                width = 124,
                font = Font(20, Color(0x000000), "Arial", Font.FontWeight.BOLD),
                formatFunction = { player -> player.name } // Just show the player's name as is
            ),
            // Column for leaf scores
            TableColumn(
                title = "Leaf",
                width = 124,
                font = Font(20, Color(0x000000), "Arial", Font.FontWeight.NORMAL),
                formatFunction = { player -> player.eachScore[0].toString() }
            ),
            // Column for leaf scores
            TableColumn(
                title = "Fruit",
                width = 124,
                font = Font(20, Color(0x000000), "Arial", Font.FontWeight.NORMAL),
                formatFunction = { player -> player.eachScore[1].toString() }

            ),
            // Column for leaf scores
            TableColumn(
                title = "Flower",
                width = 124,
                font = Font(20, Color(0x000000), "Arial", Font.FontWeight.NORMAL),
                formatFunction = { player -> player.eachScore[2].toString() }
            ),
            // Column for goal scores
            TableColumn(
                title = "Goals",
                width = 124,
                font = Font(20, Color(0x000000), "Arial", Font.FontWeight.NORMAL),
                formatFunction = { player -> player.eachScore[3].toString() }

            ),

            // Column for parchment scores
            TableColumn(
                title = "Parchment",
                width = 124,
                font = Font(20, Color(0x000000), "Arial", Font.FontWeight.NORMAL),
                formatFunction = { player -> player.eachScore[4].toString() }

            ),
            // Column for parchment scores
            TableColumn(
                title = "SUM",
                width = 124,
                font = Font(20, Color(0x000000), "Arial", Font.FontWeight.NORMAL),
                formatFunction = { player -> player.eachScore[5].toString() }
            )
        ),
        items = listOf(),
        visual = ColorVisual.BLACK.apply { transparency = 0.5 },
        selectionMode = SelectionMode.SINGLE
    )

    // Initialize the scene
    init {
        addComponents(titleLabel, resultLabel, backToMenuButton, quitButton, scoreBoard, scoreImg)
    }

    /**
     * The refreshAfterShowWinner method is called by the service layer after a game has ended.
     * It sets the result of the game
     *
     * @param players is the list of players at the end
     */
    override fun refreshAfterShowWinner(players: List<Player>) {
        // Calculate scores for each player
        val playerScores = players.map { player ->
            PlayerScore(
                name = player.name,
                eachScore = rootService.gameService.calculateScore(player) // calculateScore returns List<Int>
            )
        }

        // Set winner name
        resultLabel.text = "Winner: ${players[0].name}"

        scoreBoard.items.addAll(playerScores)
    }

    /**
     * Needed for result table
     */
    data class PlayerScore(
        val name: String,
        val eachScore: List<Int>
    )
}
