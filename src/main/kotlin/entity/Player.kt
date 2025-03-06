package entity

data class Player(val name : String ,
                  val playerType: PlayerType,
                  val isLocal: Boolean)
{
    val bonsaiTree : MutableMap<Pair<Int,Int> , Tile> = mutableMapOf()
    val personalSupply    : MutableList<Tile>     = mutableListOf()
    val collectedCards    : MutableList<Card>     = mutableListOf()
    val claimedGoals      : MutableList<GoalTile> = mutableListOf()
    val renouncedGoals    : MutableList<GoalTile> = mutableListOf()
    val playableTiles     : MutableList<TileType> = mutableListOf(TileType.ANY, TileType.WOOD, TileType.LEAF)
    var playableTilesCopy : MutableList<TileType> = mutableListOf()

    var tileCapacity = 5
    var score = 0
    var hasPlayed : Boolean = false
}
