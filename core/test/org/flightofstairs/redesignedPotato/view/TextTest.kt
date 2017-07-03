package org.flightofstairs.redesignedPotato.view

import org.flightofstairs.redesignedPotato.monsters
import org.hamcrest.Matchers.matchesPattern
import org.junit.Assert.assertThat
import org.junit.Test

class TextTest {
    @Test
    fun rollAttack() {
        val attack = monsters["Bugbear Chief"]!!
                .actions.find { it.name == "Javelin" }!!
                .attacks.findLast { it.type == "Melee" }!!

        val expected = "15 to hit. \\d{1,2} Piercing damage."
        assertThat(Text.rollAttack(attack, 10), matchesPattern(expected))
    }

    @Test
    fun rollAttacks_multipleAttacks() {
        val action = monsters["Bugbear Chief"]!!
                .actions.find { it.name == "Javelin" }!!

        val expected = "Javelin\n\nMelee: \\d{1,2} to hit. \\d{1,2} Piercing damage.\nRanged: \\d{1,2} to hit. \\d{1,2} Piercing damage."
        assertThat(Text.rollAttacks(action), matchesPattern(expected))
    }

    @Test
    fun rollAttacks_singleAttack() {
        val action = monsters["Bugbear Chief"]!!
                .actions.find { it.name == "Morningstar" }!!

        val expected = "Morningstar\n\n\\d{1,2} to hit. \\d{1,2} Piercing damage."
        assertThat(Text.rollAttacks(action), matchesPattern(expected))
    }

    @Test
    fun rollAttacks_multipleDamage() {
        val action = monsters["Mummy Lord"]!!
                .actions.find { it.name == "Rotting Fist" }!!

        val expected = "Rotting Fist\n\n\\d{1,2} to hit. \\d{1,2} Bludgeoning, \\d{1,2} Necrotic damage."
        assertThat(Text.rollAttacks(action), matchesPattern(expected))
    }
}
