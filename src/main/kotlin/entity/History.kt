package entity

data class History(var currentPosition : Int){
    val gameStates = mutableListOf<BonsaiGameState>()
}
