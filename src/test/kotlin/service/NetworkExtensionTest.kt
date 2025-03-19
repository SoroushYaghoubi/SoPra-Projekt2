package service

import edu.udo.cs.sopra.ntf.CardTypeMessage
import edu.udo.cs.sopra.ntf.ColorTypeMessage
import edu.udo.cs.sopra.ntf.GoalTileTypeMessage
import edu.udo.cs.sopra.ntf.TileTypeMessage
import entity.*
import kotlin.test.*

class NetworkExtensionTest {

    /**
     * Test for [ColorTypeMessage.toColor]
     */
    @Test
    fun testColorTypeMessageToColor(){
        assertEquals(ColorTypeMessage.BLUE.toColor(), ColorType.BLUE)
        assertEquals(ColorTypeMessage.PURPLE.toColor(), ColorType.PURPLE)
        assertEquals(ColorTypeMessage.BLACK.toColor(), ColorType.BLACK)
        assertEquals(ColorTypeMessage.RED.toColor(), ColorType.RED)
    }

    /**
     * Test for [ColorType.toColorMessage]
     */
    @Test
    fun testColorToColorTypeMessage(){
        assertEquals(ColorType.BLUE, ColorTypeMessage.BLUE.toColor())
        assertEquals(ColorType.PURPLE, ColorTypeMessage.PURPLE.toColor())
        assertEquals(ColorType.BLACK, ColorTypeMessage.BLACK.toColor())
        assertEquals(ColorType.RED, ColorTypeMessage.RED.toColor())
    }

    /**
     * Test for [TileTypeMessage.toTileType]
     */
    @Test
    fun testTileTypeMessageToTile(){
        assertEquals(TileTypeMessage.WOOD.toTileType(), TileType.WOOD)
        assertEquals(TileTypeMessage.LEAF.toTileType(), TileType.LEAF)
        assertEquals(TileTypeMessage.FLOWER.toTileType(), TileType.FLOWER)
        assertEquals(TileTypeMessage.FRUIT.toTileType(), TileType.FRUIT)
    }

    /**
     * Test for [TileType.toTileTypeMessage]
     */
    @Test
    fun testTileToTileTypeMessage(){
        assertEquals(TileType.WOOD, TileTypeMessage.WOOD.toTileType())
        assertEquals(TileType.LEAF, TileTypeMessage.LEAF.toTileType())
        assertEquals(TileType.FLOWER, TileTypeMessage.FLOWER.toTileType())
        assertEquals(TileType.FRUIT, TileTypeMessage.FRUIT.toTileType())
    }

    /**
     * Test for [CardTypeMessage.toCardType]
     */
    @Test
    fun testCardTypeMessageToCard(){
        assertEquals(CardTypeMessage.TOOL.toCardType(), CardType.TOOLCARD)
        assertEquals(CardTypeMessage.GROWTH.toCardType(), CardType.GROWTHCARD)
        assertEquals(CardTypeMessage.PARCHMENT.toCardType(), CardType.PARCHMENTCARD)
        assertEquals(CardTypeMessage.HELPER.toCardType(), CardType.HELPERCARD)
        assertEquals(CardTypeMessage.MASTER.toCardType(), CardType.MASTERCARD)
    }

    /**
     * Test for [CardType.toCardTypeMessage]
     */
    @Test
    fun testCardToCardTypeMessage(){
        assertEquals(CardType.TOOLCARD, CardTypeMessage.TOOL.toCardType())
        assertEquals(CardType.GROWTHCARD, CardTypeMessage.GROWTH.toCardType())
        assertEquals(CardType.PARCHMENTCARD, CardTypeMessage.PARCHMENT.toCardType())
        assertEquals(CardType.HELPERCARD, CardTypeMessage.HELPER.toCardType())
        assertEquals(CardType.MASTERCARD, CardTypeMessage.MASTER.toCardType())
    }

    /**
     * Test for [GoalTileTypeMessage.toGoalTileType]
     */
    @Test
    fun testGoalTileTypeMessageToGoal(){
        assertEquals(GoalTileTypeMessage.BLUE.toGoalTileType(), GoalTileType.BLUE)
        assertEquals(GoalTileTypeMessage.GREEN.toGoalTileType(), GoalTileType.GREEN)
        assertEquals(GoalTileTypeMessage.BROWN.toGoalTileType(), GoalTileType.BROWN)
        assertEquals(GoalTileTypeMessage.PINK.toGoalTileType(), GoalTileType.PINK)
        assertEquals(GoalTileTypeMessage.ORANGE.toGoalTileType(), GoalTileType.ORANGE)
    }

    /**
     * Test for [GoalTileType.toGoalTileTypeMessage]
     */
    @Test
    fun testGoalToGoalTileTypeMessage(){
        assertEquals(GoalTileType.BLUE, GoalTileTypeMessage.BLUE.toGoalTileType())
        assertEquals(GoalTileType.GREEN, GoalTileTypeMessage.GREEN.toGoalTileType())
        assertEquals(GoalTileType.BROWN, GoalTileTypeMessage.BROWN.toGoalTileType())
        assertEquals(GoalTileType.PINK, GoalTileTypeMessage.PINK.toGoalTileType())
        assertEquals(GoalTileType.ORANGE, GoalTileTypeMessage.ORANGE.toGoalTileType())
    }
}
