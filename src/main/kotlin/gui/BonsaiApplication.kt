package gui

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.util.Font

/**
 * The [BonsaiApplication] is a [BoardGameApplication] that is the main class of the application
 */
class BonsaiApplication : BoardGameApplication("Bonsai", 1920, 1080), Refreshable {

    private val rootService = RootService()
    private val mainMenuScene = MainMenuScene(this)
    private val gameScene = BonsaiGameScene(rootService)
    private val configureGameMenuScene = ConfigureGameMenuScene(this, rootService)
    private val joinScene = JoinScene(this)
    private val hostScene = HostScene()
    private val waitingScene = WaitingScene()
    private val startSessionScene = StartSessionScene(this)
    private val showResultScene = ResultScene(this, rootService)

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
        this.showMenuScene(mainMenuScene)
    }

    fun showMainMenuScene() = this.showMenuScene(mainMenuScene)
    fun showConfigScene() = this.showMenuScene(configureGameMenuScene)
    fun showJoinScene() = this.showMenuScene(joinScene)
    fun showHostScene() = this.showMenuScene(hostScene)
    fun showStartSessionScene() = this.showMenuScene(startSessionScene)
    fun showWaitingScene() = this.showMenuScene(waitingScene)
    fun showGameScene() = this.showGameScene(gameScene)

}
