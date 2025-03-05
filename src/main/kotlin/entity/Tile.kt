package entity

data class Tile(var q : Int, var r : Int, val tileType: TileType) {
    var s = -q-r
}
