package entity

/**
 * Represents a bonsai tile, which has shape of a hexagon
 *
 * @param q hexagonal coordinate
 * @param r hexagonal coordinate
 * @param s hexagonal coordinate
 * @param tileType is bonsai tile type
 */

data class Tile(var q : Int, var r : Int, val tileType: TileType) {
    val s: Int
        get() = -q-r
}
