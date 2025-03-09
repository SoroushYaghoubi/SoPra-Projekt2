package gui

interface Refreshable {

    /**
     * perform refreshes that are necessary after the game has started
     */
    fun refreshAfterGameStart(){}

    /**
     * perform refreshes that are necessary after the game has ended
     */
    fun refreshAfterGameEnd(){}

    /**
     * perform refreshes that are necessary after a player has played a tile
     */
    fun refreshAfterPlayTile(){}

    /**
     * perform refreshes that are necessary after a player has meditated
     */
    fun refreshAfterMeditate(){}

    /**
     * perform refreshes that are necessary after a player has chosen a card
     */
    fun refreshAfterChooseCard(){}

    /**
     * perform refreshes that are necessary after a player has chosen a tile
     */
    fun refreshAfterChooseTile(){}

    /**
     * perform refreshes that are necessary after a player has ended his turn
     */
    fun refreshAfterEndTurn(){}

    /**
     * perform refreshes that are necessary after a player has discarded tile
     */
    fun refreshAfterDiscardTile(){}

    /**
     * perform refreshes that are necessary after a player has removed tiles from his tree
     */
    fun refreshAfterRemoveFromTree(){}

    /**
     * perform refreshes that are necessary a player has claimed a goal tile
     */
    fun refreshAfterClaimGoal(){}

    /**
     * perform refreshes that are necessary after a player has redone or undone his turn
     */
    fun refreshAfterRedoOrUndo(){}

}
