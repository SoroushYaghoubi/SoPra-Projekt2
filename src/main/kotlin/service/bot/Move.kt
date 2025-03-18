package service.bot

import entity.GoalTile
import entity.TileType

class Move(
    val removedTiles : MutableList<Pair<Int,Int>> = mutableListOf(),
    val actionType : Int? = null,
    val takenCard : Int? = null,
    val chosenTiles : MutableList<TileType> = mutableListOf(),
    val takenGoalTile: MutableList<GoalTile> = mutableListOf(),
    val renouncedGoalTile: MutableList<GoalTile> = mutableListOf(),
    val playedTiles : MutableList<Pair<TileType,Pair<Int,Int>>>,
    val chosenRemoveTiles : MutableList<Pair<TileType,Pair<Int,Int>>>,
) {

}
