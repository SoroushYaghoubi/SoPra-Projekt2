package gui

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.util.Font

/**
 * The [BonsaiApplication] is a [BoardGameApplication] that is the main class of the application
 */
class BonsaiApplication : BoardGameApplication("Bonsai", 1920,1080) {

    private val mainMenuScene = MainMenuScene(this)
    private val gameScene = BonsaiGameScene()
    private val configureGameMenuScene = ConfigureGameMenuScene(this)
    private val joinScene = JoinScene()
    private val hostScene = HostScene()

    init {
        loadFont("arial_black.ttf", "Arial Black" , Font.FontWeight.NORMAL)
        this.showGameScene(gameScene)
        this.showMenuScene(mainMenuScene)
    }

    fun showMainMenuScene() = this.showMenuScene(mainMenuScene)
    fun showConfigScene() = this.showMenuScene(configureGameMenuScene)
    fun showJoinScene() = this.showMenuScene(joinScene)
    fun showHostScene() = this.showMenuScene(hostScene)

}
