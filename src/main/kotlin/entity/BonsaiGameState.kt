package entity

data class BonsaiGameState(var currentPlayer : Player , val botSpeed : Int ,
                           var endGameCounter : Int , var currentState : States ){

    val zenDeck: MutableList<Card> = mutableListOf()
    val faceUpCards : MutableList<Card> = mutableListOf()
    val goalTile : MutableList<GoalTile> = mutableListOf()
}



