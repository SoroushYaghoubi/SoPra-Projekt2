package entity

enum class States {
    START_TURN,
    END_TURN,

    REMOVE_TILES,
    DISCARDING,
    CHOOSE_ACTION,

    MEDITATE,
    CULTIVATE,

    USING_HELPER,
    USING_MASTER,
}
