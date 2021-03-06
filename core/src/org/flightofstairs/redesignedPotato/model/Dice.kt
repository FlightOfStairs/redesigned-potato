package org.flightofstairs.redesignedPotato.model

import java.lang.Math.abs
import java.security.SecureRandom

val random = SecureRandom()

sealed class DiceExpression {
    fun average(): Int = averagePrecise().toInt()

    fun roll(): Int = when(this) {
        is RollModifier -> number
        is Dice -> IntRange(1, count).sumBy { random.nextInt(sides) + 1 }
        is Sum -> subexpressions.sumBy { it.roll() }
        is Negation -> 0 - negatedExpression.roll()
    }

    internal fun averagePrecise(): Double = when (this) {
        is RollModifier -> number.toDouble()
        is Dice -> (1.0 + sides) / 2 * count // The average of the terms of a contiguous subsequence of any arithmetic progression is the average of the first and last terms.
        is Sum -> subexpressions.sumByDouble { it.averagePrecise() }
        is Negation -> 0 - negatedExpression.averagePrecise()
    }

    companion object {
        val d20: DiceExpression = Dice(1, 20)

        fun fromString(roll: String): DiceExpression {
            val expression = roll
                    .toLowerCase()
                    .replace("plus", "+")
                    .filter { !it.isWhitespace() }
                    .let { if (it.first() == '(' && it.last() == ')') it.substring(1, it.length - 1) else it }

            return fromCleanString(expression, false)
        }

        private fun fromCleanString(expression: String, negated: Boolean): DiceExpression {
            // assumes no complete expressions that are negative
            if (Regex("\\d+").matches(expression)) return RollModifier(expression.toInt().let { if (negated) 0 - it else it })

            if (expression.contains('+')) {
                return Sum(expression.split('+').map { fromCleanString(it, false) }).let { if (negated) Negation(it) else it }
            }

            if (expression.contains('-')) {
                if (expression.count { it == '-' } != 1) throw RuntimeException("Only one negation permitted at this point.")
                if (negated) throw RuntimeException("Double negation is not supported")
                val (left, right) = expression.split('-')
                return Sum(listOf(fromCleanString(left, negated), fromCleanString(right, true)))
            }

            if (expression.contains('d')) {
                if (expression.count { it == 'd' } != 1) throw RuntimeException("Only one size of dice permitted at this point.")
                val (count, sides) = expression.split('d')
                return Dice(count.toInt(), sides.toInt()).let { if (negated) Negation(it) else it }
            }

            throw RuntimeException("Weird dice (sub)expression: $expression")
        }
    }

}

internal data class RollModifier(val number: Int) : DiceExpression() {
    override fun toString() = abs(number).toString()
}
internal data class Dice(val count: Int, val sides: Int) : DiceExpression() {
    override fun toString() = "${count}d${sides}"
}
internal data class Sum(val subexpressions: List<DiceExpression>) : DiceExpression() {
    override fun toString(): String {
        val head = subexpressions[0]
        val tail = subexpressions.subList(1, subexpressions.size)

        assert(head !is Negation)
        assert(head !is RollModifier || head.number >= 0)

        val strings = listOf(head.toString()) + tail.flatMap {
                if (it is Negation) listOf(it.toString())
                else if (it is RollModifier && it.number < 0) listOf("-", it.toString())
                else listOf("+", it.toString())
        }
        return strings.joinToString(" ")
    }
}
internal data class Negation(val negatedExpression: DiceExpression) : DiceExpression() {
    override fun toString() = "- $negatedExpression"
}
