package util

import entity.Tile
import entity.TileType

fun main() {
    // example case of why having map coordinates AND tile coordinates is error-prone
    val tree = mutableMapOf<Pair<Int, Int>, Tile>(
        Pair(0, 0) to Tile(0, 0, TileType.WOOD),    // center
        Pair(1, -1) to Tile(0, 0, TileType.LEAF),   // top right
        Pair(1, 0) to Tile(0, 0, TileType.FLOWER),  // right
        Pair(0, 1) to Tile(0, 0, TileType.EMPTY),   // bottom right
                                                          // bottom left (null)
        Pair(-1, 0) to Tile(0, 0, TileType.FRUIT),  // left
        Pair(0, -1) to Tile(0, 0, TileType.ANY),    // top left
    )

    // example use case of core utils
    for (tile in tree around Pair(0, 0)) {
        println(tile)
    }
}