package util

import entity.Tile

/**
 * A wrapper function that checks if the Tile placed on a position has the correct inner coordinates.
 */
fun MutableMap<Pair<Int,Int>, Tile?>.safelyAppendTile(tile: Tile, position: Pair<Int, Int>) {
    require(tile.q == position.first && tile.r == position.second)
        { "You can't place tile with inner positions (${tile.q}, ${tile.r}) on (${position.first}, ${position.second})" }

    this@safelyAppendTile[position] = tile
}