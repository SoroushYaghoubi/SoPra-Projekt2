package entity

data class Tile(var q : Int, var r : Int, val tileType: TileType) {
    val s: Int
        get() = -q-r
}
