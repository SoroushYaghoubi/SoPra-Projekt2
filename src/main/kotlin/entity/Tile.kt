package entity

import kotlinx.serialization.Serializable
/**
 * Represents a bonsai [Tile], which has shape of a hexagon
 *
 * @param q hexagonal coordinate
 * @param r hexagonal coordinate
 * @param s hexagonal coordinate
 * @param tileType is bonsai [TileType]
 */
@Serializable
data class Tile(val q: Int? = null, val r: Int? = null, val tileType: TileType) {
    val s: Int?
        get() = if (q != null && r != null) (-q - r) else null

    override fun toString(): String {
        return "Tile Type: ${this.tileType}"
    }
}
