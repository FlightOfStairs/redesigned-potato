package org.flightofstairs.redesignedPotato.model

sealed class DiceExpression {

    fun average(): Double = when (this) {
        is Modifier -> number.toDouble()
        is Dice -> (1.0 + sides) / 2 * count // The average of the terms of a contiguous subsequence of any arithmetic progression is the average of the first and last terms.
        is Sum -> subexpressions.sumByDouble { it.average() }
        is Negation -> 0 - negatedExpression.average()
    }

    companion object {
        fun fromString(roll: String): DiceExpression {
            val expression = roll
                    .toLowerCase()
                    .replace("plus", "+")
                    .filter { !it.isWhitespace() }

            return fromCleanString(expression)
        }

        private fun fromCleanString(expression: String): DiceExpression {
            // assumes no complete expressions that are negative
            if (expression.toCharArray().all { it.isDigit() }) return Modifier(expression.toInt())

            if (expression.contains('+')) {
                return Sum(expression.split('+').map { fromCleanString(it) })
            }

            if (expression.contains('-')) {
                assert(expression.count { it == '-' } == 1)
                val (left, right) = expression.split('-')
                return Sum(listOf(fromCleanString(left), Negation(fromCleanString(right))))
            }

            if (expression.contains('d')) {
                assert(expression.count { it == 'd' } == 1)
                val (count, sides) = expression.split('d')
                return Dice(count.toInt(), sides.toInt())
            }

            throw RuntimeException("Weird dice (sub)expression: $expression")
        }
    }

}

internal data class Modifier(val number: Int) : DiceExpression()
internal data class Dice(val count: Int, val sides: Int) : DiceExpression()
internal data class Sum(val subexpressions: List<DiceExpression>) : DiceExpression()
internal data class Negation(val negatedExpression: DiceExpression) : DiceExpression()
