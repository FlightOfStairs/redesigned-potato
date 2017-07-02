package org.flightofstairs.redesignedPotato.model

import org.flightofstairs.redesignedPotato.model.EncounterDifficulty.*

data class Thresholds(val easy: Int, val medium: Int, val hard: Int, val deadly: Int) {
    operator fun plus(other: Thresholds) = Thresholds(easy + other.easy, medium + other.medium, hard + other.hard, deadly + other.deadly)
}


enum class EncounterDifficulty { Easy, Medium, Hard, Deadly, }

// See DMG p82-83
data class Encounter(val players: List<PlayerCharacter>, val monsters: List<MonsterInfo>) {
    val partyThresholds = players.map { levelXpThresholds[it.level] ?: throw IllegalArgumentException("Not a valid level: ${it.level}") }.reduce { left, right -> left + right }
    val monsterXp = monsters.sumBy { it.xp }
    val encounterMultiplier = encounterMultiplier(players.size, monsters.size)
    val adjustedXp = (monsterXp * encounterMultiplier).toInt()

    val encounterDifficulty = when {
        adjustedXp >= partyThresholds.deadly -> Deadly
        adjustedXp >= partyThresholds.hard -> Hard
        adjustedXp >= partyThresholds.deadly -> Medium
        else -> Easy
    }

    companion object {
        private fun encounterMultiplier(players: Int, monsters: Int): Double {
            val multipliers = listOf(0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0)

            val position = when (monsters) {
                1 -> 1
                2 -> 2
                3, 4, 5, 6 -> 3
                7, 8, 9, 10 -> 4
                11, 12, 13, 14 -> 5
                else -> 6
            } + when {
                players < 3 -> 1
                players > 5 -> -1
                else -> 0
            }

            return multipliers[minOf(position, multipliers.size - 1)]
        }
    }
}


internal val levelXpThresholds = mapOf(
        1 to Thresholds(25, 50, 75, 100),
        2 to Thresholds(50, 100, 150, 200),
        3 to Thresholds(75, 150, 225, 400),
        4 to Thresholds(125, 250, 375, 500),
        5 to Thresholds(250, 500, 750, 1100),
        6 to Thresholds(300, 600, 900, 1400),
        7 to Thresholds(350, 750, 1100, 1700),
        8 to Thresholds(450, 900, 1400, 2100),
        9 to Thresholds(550, 1100, 1600, 2400),
        10 to Thresholds(600, 1200, 1900, 2800),
        11 to Thresholds(800, 1600, 2400, 3600),
        12 to Thresholds(1000, 2000, 3000, 4500),
        13 to Thresholds(1100, 2200, 3400, 5100),
        14 to Thresholds(1250, 2500, 3800, 5700),
        15 to Thresholds(1400, 2800, 4300, 6400),
        16 to Thresholds(1600, 3200, 4800, 7200),
        17 to Thresholds(2000, 3900, 5900, 8800),
        18 to Thresholds(2100, 4200, 6300, 9500),
        19 to Thresholds(2400, 4900, 7300, 10900),
        20 to Thresholds(2800, 5700, 8500, 12700))
