package org.flightofstairs.redesignedPotato.model

import org.junit.Assert.assertEquals
import org.junit.Test

class DiceExpressionTest {

    @Test
    fun parse() {
        assertEquals(Modifier(5), DiceExpression.fromString("5"))
        assertEquals(Dice(3, 6), DiceExpression.fromString("3d6"))
        assertEquals(Sum(listOf(Dice(3, 6), Modifier(4))), DiceExpression.fromString("3d6 + 4"))
        assertEquals(Sum(listOf(Dice(1, 4), Modifier(-1))), DiceExpression.fromString("1d4 - 1"))
        assertEquals(Sum(listOf(Dice(2, 8), Negation(Dice(2, 4)))), DiceExpression.fromString("2d8 - 2d4"))
        assertEquals(Sum(listOf(Dice(1, 12), Modifier(4), Dice(1, 8))), DiceExpression.fromString("1d12 + 4 plus 1d8"))
    }

    @Test
    fun average_modifier() = assertEquals(5, Modifier(5).average())

    @Test
    fun average_singleDice() {
        assertEquals(1, Dice(1, 1).average())
        assertEquals(1, Dice(1, 2).average())
        assertEquals(2, Dice(1, 4).average())
    }

    @Test
    fun average_multipleDice() {
        assertEquals(3, Dice(2, 2).average())
        assertEquals(10, Dice(3, 6).average())
    }

    @Test
    fun average_expression() {
        assertEquals(14.5, DiceExpression.fromString("3d6 + 4").averagePrecise(), 0.01)
        assertEquals(1.5, DiceExpression.fromString("1d4 - 1").averagePrecise(), 0.01)
        assertEquals(15.0, DiceExpression.fromString("1d12 + 4 plus 1d8").averagePrecise(), 0.01)
    }

    @Test
    fun expressionToString() {
        assertEquals("3d6 + 4", DiceExpression.fromString("3d6 + 4").toString())
        assertEquals("1d4 - 1", DiceExpression.fromString("1d4 - 1").toString())
        assertEquals("1d12 + 4 + 1d8", DiceExpression.fromString("1d12 + 4 plus 1d8").toString())

        assertEquals("1d12 - 1d4 + 1", DiceExpression.fromString("1d12 - 1d4 + 1").toString())
    }
}
