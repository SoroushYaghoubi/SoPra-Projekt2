package util

import entity.Tile

/**
 * Takes an axial coordinate as a parameter and acts like an iterator. It returns a generator that yields a tile around
 * the center one by one.
 *
 * @param center is the coordinate around which we are iterating
 *
 * @return Tile
 * @return null if there is no tile in that spot.
 *
 * @see `Demo` in the same dir for example usage.
 */
infix fun MutableMap<Pair<Int, Int>, Tile>.around(center: Pair<Int, Int>): Sequence<Tile?> = sequence {
    val (q, r) = center

    val directions = listOf(
        Pair(1, -1),   // Up-Right
        Pair(1, 0),   // Right
        Pair(0, 1),   // Down-Right
        Pair(-1, 1),  // Down-Left
        Pair(-1, 0),  // Left
        Pair(0, -1),  // Up-Left
    )

    for ((dq, dr) in directions) {
        val coordinate = (q + dq) to (r + dr)
        yield(this@around[coordinate])
    }
}