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
    fun average_modifier() = assertEquals(5, Modifier(5).average().toInt())

    @Test
    fun average_singleDice() {
        assertEquals(1.0, Dice(1, 1).average(), 0.01)
        assertEquals(1.5, Dice(1, 2).average(), 0.01)
        assertEquals(2.5, Dice(1, 4).average(), 0.01)
    }

    @Test
    fun average_multipleDice() {
        assertEquals(3.0, Dice(2, 2).average(), 0.01)
        assertEquals(10.5, Dice(3, 6).average(), 0.01)
    }

    @Test
    fun average_expression() {
        assertEquals(14.5, DiceExpression.fromString("3d6 + 4").average(), 0.01)
        assertEquals(1.5, DiceExpression.fromString("1d4 - 1").average(), 0.01)
        assertEquals(15.0, DiceExpression.fromString("1d12 + 4 plus 1d8").average(), 0.01)
    }
}
