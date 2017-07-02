package org.flightofstairs.redesignedPotato.view

import org.flightofstairs.redesignedPotato.model.DiceExpression
import org.flightofstairs.redesignedPotato.model.MonsterAction
import org.flightofstairs.redesignedPotato.model.MonsterAttack

class Text {
    companion object {
        fun rollAttacks(action: MonsterAction): String {
            val attacks = action.attacks
            assert(attacks.isNotEmpty())

            val toHitRollRaw = DiceExpression.d20.roll()

            val attackOutcomes = if (attacks.size == 1) {
                rollAttack(attacks[0], toHitRollRaw)
            } else {
                attacks.map { "${it.type}: ${rollAttack(it, toHitRollRaw)}" }.joinToString("\n")
            }

            return "${action.name}\n\n$attackOutcomes"
        }

        fun rollAttack(attack: MonsterAttack, rawAttackRoll: Int): String {
            val toHit = rawAttackRoll + attack.toHitModifier.number
            if (attack.damages.isEmpty()) return "$toHit to hit."

            val damageText = attack.damages
                    .filter {it.diceExpression != null}
                    .map { "${it.diceExpression!!.roll()} ${it.type}" }
                    .joinToString()

            return "$toHit to hit. $damageText damage."
        }
    }
}
