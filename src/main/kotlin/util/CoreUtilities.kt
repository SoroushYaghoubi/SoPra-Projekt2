package util

import entity.Tile


// some constants (non-primitives cannot be const todo: what to do?)
val VECTOR_TOP_RIGHT        = (1 to -1)
val VECTOR_RIGHT            = (1 to 0)
val VECTOR_BOTTOM_RIGHT     = (0 to 1)
val VECTOR_BOTTOM_LEFT      = (-1 to 1)
val VECTOR_LEFT             = (-1 to 0)
val VECTOR_TOP_LEFT         = (0 to -1)

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

    val sideVectors = listOf(
        VECTOR_TOP_RIGHT,
        VECTOR_RIGHT,
        VECTOR_BOTTOM_RIGHT,
        VECTOR_BOTTOM_LEFT,
        VECTOR_LEFT,
        VECTOR_TOP_LEFT
    )

    for ((dq, dr) in sideVectors) {
        val coordinate = (q + dq) to (r + dr)
        yield(this@circleAround[coordinate])
    }
}

/**
 * Wrapper for the [circleAround] function to iterate infinitely around a coordinate
 */
infix fun MutableMap<Pair<Int, Int>, Tile>.foreverCircleAround(center: Pair<Int, Int>): Sequence<Tile?> = sequence {
    while (true) {
        for (tile in this@foreverCircleAround circleAround center) {
            yield(tile)
        }
    }
}

