package service

import entity.*

fun BonsaiGameState.deepCopy(): BonsaiGameState {
    return BonsaiGameState(
        currentPlayer = currentPlayer.deepCopy(),
        players = players.map { it.deepCopy() }.toMutableList(),
        botSpeed = botSpeed,
        currentState = currentState
    ).also { copy ->
        copy.endGameCounter = this.endGameCounter
        copy.zenDeck = this.zenDeck.map { it.deepCopy() }.toMutableList()
        copy.faceUpCards = this.faceUpCards.map { it.deepCopy() }.toMutableList()
        copy.goalTiles = this.goalTiles.map { it.deepCopy() }.toMutableList()
    }
}

fun Player.deepCopy(): Player {
    return Player(
        name = this.name,
        playerType = this.playerType,
        isLocal = this.isLocal,
        color = this.color
    ).also { copy ->
        //need deep copies
        copy.bonsaiTree = this.bonsaiTree.mapValues { it.value.deepCopy() }.toMutableMap()
        copy.personalSupply = this.personalSupply.map { it.deepCopy() }.toMutableList()
        copy.collectedCards = this.collectedCards.map { it.deepCopy() }.toMutableList()
        copy.claimedGoals = this.claimedGoals.map { it.deepCopy() }.toMutableList()
        copy.renouncedGoals = this.renouncedGoals.map { it.deepCopy() }.toMutableList()

        // do not need deep copier i hope
        copy.playableTiles = this.playableTiles.toMutableList()
        copy.playableTilesCopy = this.playableTilesCopy.toMutableList()
        copy.tileCapacity = this.tileCapacity
        copy.score = this.score
        copy.hasPlayed = this.hasPlayed
    }
}

fun Tile.deepCopy(): Tile {
    return Tile(this.q, this.r, this.tileType)
}

fun GoalTile.deepCopy(): GoalTile {
    return GoalTile(this.goalTileType, this.tier, this.score)
}

fun Card.deepCopy(): Card {
    return when (this) {
        is GrowthCard -> GrowthCard(this.tileType, this.id)
        is HelperCard -> HelperCard(this.tileTypes[1], this.id)
        is MasterCard -> MasterCard(this.tileTypes.toMutableList(), this.id)
        is ParchmentCard -> ParchmentCard(this.parchmentTileType, this.parchmentCardType, this.basePoints, this.id)
        is ToolCard -> ToolCard(this.id)
        else -> throw IllegalArgumentException("cardtype does not exist: ${this::class.simpleName}")
    }
}

