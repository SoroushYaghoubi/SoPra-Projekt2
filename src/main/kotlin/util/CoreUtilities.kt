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
infix fun MutableMap<Pair<Int, Int>, Tile>.circleAround(center: Pair<Int, Int>): Sequence<Tile?> = sequence {
    val (q, r) = center

    val SIDE_VECTORS = listOf(
        (1 to -1),  // Up-Right
        (1 to 0),   // Right
        (0 to 1),   // Down-Right
        (-1 to 1),  // Down-Left
        (-1 to  0), // Left
        (0 to -1),  // Up-Left
    )

    for ((dq, dr) in SIDE_VECTORS) {
        val coordinate = (q + dq) to (r + dr)
        yield(this@circleAround[coordinate])
    }
}

infix fun MutableMap<Pair<Int, Int>, Tile>.foreverCircleAround(center: Pair<Int, Int>): Sequence<Tile?> = sequence {
    while (true) {
        for (tile in this@foreverCircleAround circleAround center) {
            yield(tile)
        }
    }
}
