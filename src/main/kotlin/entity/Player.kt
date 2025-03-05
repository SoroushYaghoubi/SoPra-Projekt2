package entity

data class Player(val name : String ,
                  val playerType: PlayerType,
                  var isLocal : Boolean = true)
{

    var tileCapacity : Int = 5
    val playableTiles : MutableList<TileType> = mutableListOf()
    val playableTilesCopy : MutableList<TileType> = mutableListOf()
    val renouncedCoals :  MutableList<GoalTile> = mutableListOf()
    val bonsaiTree : MutableMap<Pair<Int,Int> , Tile> = mutableMapOf()
    var score : Int = 0
    var hasPlayed : Boolean = false
    val collectedCards : MutableList<Card> = mutableListOf()
    val claimedGoals : MutableList<GoalTile> = mutableListOf()
    val personalSupply :  MutableList<Tile> = mutableListOf()


}
