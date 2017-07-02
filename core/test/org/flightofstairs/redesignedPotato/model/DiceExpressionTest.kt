package org.flightofstairs.redesignedPotato.model

import org.junit.Assert.assertEquals
import org.junit.Test

class DiceExpressionTest {
    @Test
    fun average_modifier() = assertEquals(5, DiceExpression.fromString("5").average())

    @Test
    fun average_singleDice() {
        assertEquals(1, DiceExpression.fromString("1d1").average())
        assertEquals(1, DiceExpression.fromString("1d2").average())
        assertEquals(2, DiceExpression.fromString("1d4").average())
    }

    @Test
    fun average_multipleDice() {
        assertEquals(3, DiceExpression.fromString("2d2").average())
        assertEquals(10, DiceExpression.fromString("3d6").average())
    }

    @Test
    fun average_expression() {
        assertEquals(14.5, DiceExpression.fromString("3d6 + 4").averagePrecise(), 0.01)
        assertEquals(1.5, DiceExpression.fromString("1d4 - 1").averagePrecise(), 0.01)
        assertEquals(15.0, DiceExpression.fromString("1d12 + 4 plus 1d8").averagePrecise(), 0.01)

        // differs from book, but agrees with http://anydice.com/
        assertEquals(493, DiceExpression.fromString("34d12 + 272").average())
    }

    @Test
    fun expressionToString() {
        assertEquals("1", DiceExpression.fromString("1").toString())

        assertEquals("3d6 + 4", DiceExpression.fromString("3d6 + 4").toString())
        assertEquals("1d4 - 1", DiceExpression.fromString("1d4 - 1").toString())
        assertEquals("1d12 + 4 + 1d8", DiceExpression.fromString("1d12 + 4 plus 1d8").toString())

        assertEquals("1d12 - 1d4 + 1", DiceExpression.fromString("1d12 - 1d4 + 1").toString())
    }
}
