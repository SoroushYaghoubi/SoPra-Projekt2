package util

import entity.Tile
import entity.TileType

fun main() {
    println("Example case of why having map coordinates AND tile coordinates is error-prone: ")
    val tree = mutableMapOf(
        (0 to 0) to Tile(0, 0, TileType.WOOD),    // center
        (1 to -1) to Tile(0, 0, TileType.LEAF),   // top right
        (1 to 0) to Tile(0, 0, TileType.FLOWER),  // right
        (0 to 1) to Tile(0, 0, TileType.EMPTY),   // bottom right
                                                        // bottom left (null)
        (-1 to 0) to Tile(0, 0, TileType.FRUIT),  // left
        (0 to -1) to Tile(0, 0, TileType.ANY),    // top left
    )

    println("Example use case of iterating the side tiles once: ")
    for (tile in tree circleAround (0 to 0)) {
        println(tile)
    }

    println("Example use case of iterating the side tiles for n times: ")
    (tree foreverCircleAround (0 to 0))
        .take(8)
        .forEach {
            println(it)
        }

    println("Example use case, manually using iterator: ")
    val sideNode = (tree foreverCircleAround (0 to 0)).iterator()
    println(sideNode)
    println(sideNode.next())

    println("Primitive axial vector manipulation relative to base point (0, 0): ")
    println("(-1 to 0) rotating clockwise around         (0 to 0): ${(-1 to 0) rotateClockwiseAround (0 to 0)}")
    println("(0 to -1) rotating counter-clockwise around (0 to 0): ${(0 to -1) rotateCounterClockwiseAround (0 to 0)}")
}
