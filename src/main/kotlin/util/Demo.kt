package util

import entity.Tile
import entity.TileType

fun main() {
    // --------------- safe append ---------------
    val tree = mutableMapOf<Pair<Int, Int>, Tile?>()
    // (some of these will be ignored on traverse because they will be on pot)
    // radius 1
    tree.safelyAppendTile(Tile(0, 0, TileType.WOOD), 0 to 0)
    tree.safelyAppendTile(Tile(1, -1, TileType.WOOD), 1 to -1)
    tree.safelyAppendTile(Tile(1, 0, TileType.WOOD), 1 to 0)
    tree.safelyAppendTile(Tile(0, 1, TileType.WOOD), 0 to 1)
    tree.safelyAppendTile(Tile(-1, 0, TileType.WOOD), -1 to 0)
    tree.safelyAppendTile(Tile(0, -1, TileType.WOOD), 0 to -1)
    // radius 2
    tree.safelyAppendTile(Tile(-2, 0, TileType.LEAF), -2 to 0)
    tree.safelyAppendTile(Tile(-1, -1, TileType.LEAF), -1 to -1)
    tree.safelyAppendTile(Tile(0, -2, TileType.LEAF), 0 to -2)
    tree.safelyAppendTile(Tile(2, 0, TileType.FLOWER), 2 to 0)
    tree.safelyAppendTile(Tile(1, 1, TileType.FLOWER), 1 to 1)
    tree.safelyAppendTile(Tile(0, 2, TileType.FLOWER), 0 to 2)

    println("\n// --------------- bfs on tree ---------------")
    println("Traversing tiles starting from base (0, 0): ")
    for (tilePosition in (tree traverseFrom (ROOT))) {
        println(tree[tilePosition])
    }

    println("\n// --------------- playable positions ---------------")
    println("List of playable positions: ")
    println(tree.getEmptyTiles())

    println("\n// --------------- single iterator ---------------")
    println("Iterating the side coordinates once: ")
    for (tile in circleAround(ROOT)) {
        println(tile)
    }

    println("\n// --------------- infinite iterator ---------------")
    println("Iterating the side coordinates for n times: ")
    (foreverCircleAround(ROOT))
        .take(8)
        .forEach {
            println(it)
        }

    println("\n// --------------- manual iteration ---------------")
    println("Manually using iterator: ")
    val axialIterator = (foreverCircleAround(ROOT)).iterator()
    println(axialIterator.next())
    println(axialIterator.next())

    println("\n// --------------- atomic axial arithmetic ---------------")
    println("Primitive axial vector manipulation relative to base point (0, 0): ")
    println("       (-1 to 0) rotating clockwise around         ROOT: ${(-1 to 0) rotateClockwiseAround (ROOT)}")
    println("       (0 to -1) rotating counter-clockwise around ROOT: ${(0 to -1) rotateCounterClockwiseAround (ROOT)}")
}
