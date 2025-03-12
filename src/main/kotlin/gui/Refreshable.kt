package gui

import entity.TileType
import service.AbstractRefreshingService

/**
 * This interface provides a mechanism for the service layer classes to communicate
 * that certain changes have been made to the entity layer, so that the user
 * can be updated accordingly
 *
 * @see AbstractRefreshingService
 */
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
     * perform refreshes that are necessary after a player has cultivated
     */
     fun refreshAfterCultivate(){}

    /**
     * perform refreshes that are necessary after a player has chosen a card
     */
    fun refreshAfterChooseCard(){}

    /**
     * perform refreshes that are necessary after a player has chosen a MasterCard
     */
    fun refreshAfterDrawingMasterCardAny(){}

    /**
     * perform refreshes that are necessary after a player has chosen a HelperCard
     */
    fun refreshAfterDrawingHelperCard(firstTileTypeToPlace : TileType , secondTileTypeToPlace : TileType ){}
    /**
     * perform refreshes that are necessary after a player has chosen a tile or has received tiles
     */
    fun refreshAfterChoseOrReceivedTile(discard : Boolean){}

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
