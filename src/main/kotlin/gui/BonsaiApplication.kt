package gui

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.util.Font

/**
 * The [BonsaiApplication] is a [BoardGameApplication] that is the main class of the application
 */
class BonsaiApplication : BoardGameApplication("Bonsai", 1920, 1080), Refreshable {

    /**
     * Central services for all scenes and holds current game as well
     */
    private val rootService = RootService()

    /**
     * Initialization of all scene
     */
    private val mainMenuScene = MainMenuScene(this)
    private val gameScene = BonsaiGameScene(rootService)
    private val configureGameMenuScene = ConfigureGameMenuScene(this, rootService)
    private val joinScene = JoinScene(this)
    private val hostScene = HostScene()
    private val waitingScene = WaitingScene()
    private val startSessionScene = StartSessionScene(this)
    private val showResultScene = ResultScene(this, rootService)

    // loads to begin the used Font
    // shows gameScene and shows the mainMenuScene
    init {
        rootService.addRefreshables(
            this,
            gameScene,
            mainMenuScene,
            waitingScene,
            configureGameMenuScene,
            joinScene,
            hostScene,
            startSessionScene,
            showResultScene
        )

        loadFont("arial_black.ttf", "Arial Black", Font.FontWeight.NORMAL)
        //this.showGameScene(gameScene)
        showMainMenuScene()
    }

    /**
     * Function [showMainMenuScene] shows the mainMenuScene
     */
    fun showMainMenuScene() = this.showMenuScene(mainMenuScene)

    /**
     * Funktion [showConfigScene] shows the configScene
     */
    fun showConfigScene() = this.showMenuScene(configureGameMenuScene)

    /**
     * Funktion [showJoinScene] shows the joinScene
     */
    fun showJoinScene() = this.showMenuScene(joinScene)

    /**
     * Funktion [showHostScene] shows the hostScene
     */
    fun showHostScene() = this.showMenuScene(hostScene)

    /**
     * Funktion [showStartSessionScene] shows the startSessionScene
     */
    fun showStartSessionScene() = this.showMenuScene(startSessionScene)

    /**
     * Funktion [showWaitingScene] shows the waitingScene
     */
    fun showWaitingScene() = this.showMenuScene(waitingScene)

    /**
     * Function [showGameScene] shows the GameScene
     */
    fun showGameScene() = this.showGameScene(gameScene)

}
