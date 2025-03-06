package entity

data class BonsaiGameState(var currentPlayer : Player ,
                           val players: MutableList<Player>,
                           val botSpeed : Int ,
                           var currentState : States){
    var endGameCounter = 0
    val zenDeck: MutableList<Card> = mutableListOf()
    val faceUpCards : MutableList<Card> = mutableListOf()
    val goalTiles : MutableList<GoalTile> = mutableListOf()
}
