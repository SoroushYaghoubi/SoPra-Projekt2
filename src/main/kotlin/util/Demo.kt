package util

import entity.Tile
import entity.TileType

fun main() {
    // example case of why having map coordinates AND tile coordinates is error-prone
    val tree = mutableMapOf(
        (0 to 0) to Tile(0, 0, TileType.WOOD),    // center
        (1 to -1) to Tile(0, 0, TileType.LEAF),   // top right
        (1 to 0) to Tile(0, 0, TileType.FLOWER),  // right
        (0 to 1) to Tile(0, 0, TileType.EMPTY),   // bottom right
                                                        // bottom left (null)
        (-1 to 0) to Tile(0, 0, TileType.FRUIT),  // left
        (0 to -1) to Tile(0, 0, TileType.ANY),    // top left
    )

    // example use case of iterating the side tiles once
    for (tile in tree circleAround (0 to 0)) {
        println(tile)
    }

    // example use case of iterating the side tiles infinitely
    (tree foreverCircleAround (0 to 0)).take(8).forEach { println(it) }

}
