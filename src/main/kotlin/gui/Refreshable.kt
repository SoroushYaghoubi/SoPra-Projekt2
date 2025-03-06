package gui

interface Refreshable {

    fun refreshAfterGameStart()

    fun refreshAfterGameEnd()

    fun refreshAfterPlayTile()

    fun refreshAfterMeditate()

    fun refreshAfterChooseCard()

    fun refreshAfterChooseTile()

    fun refreshAfterEndTurn()

    fun refreshAfterDiscardTile()

    fun refreshAfterRemoveFromTree()

    fun refreshAfterClaimGoal()

    fun refreshAfterRedoOrUndo()


}
