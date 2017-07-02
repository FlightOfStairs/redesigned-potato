package org.flightofstairs.redesignedPotato.model

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.flightofstairs.redesignedPotato.model.EncounterDifficulty.Hard
import org.junit.Test
import java.util.Collections.nCopies
import kotlin.test.assertEquals

class EncounterTest {

    @Test
    fun thresholds_dmgExample() {
        val players = listOf(
                PlayerCharacter("a", 3),
                PlayerCharacter("b", 3),
                PlayerCharacter("c", 3),
                PlayerCharacter("d", 2))

        assertEquals(Thresholds(275, 550, 825, 1400), Encounter(players, listOf()).partyThresholds)
    }

    @Test
    fun adjustedXp_dmgExample() {
        val players = nCopies(4, PlayerCharacter("a", 3))
        val monsters = nCopies(4, mock<MonsterInfo> { on { xp } doReturn 125 })

        val encounter = Encounter(players, monsters)
        assertEquals(500, encounter.monsterXp)
        assertEquals(1000, encounter.adjustedXp)
    }

    @Test
    fun adjustedXp_smallParty_dmgExample() {
        val players = nCopies(2, PlayerCharacter("a", 3))

        assertEquals((125 * 1.5).toInt(), Encounter(players, nCopies(1, mock<MonsterInfo> { on { xp } doReturn 125 })).adjustedXp)
        assertEquals(125 * 15 * 5, Encounter(players, nCopies(15, mock<MonsterInfo> { on { xp } doReturn 125 })).adjustedXp)
    }

    @Test
    fun adjustedXp_largeParty_dmgExample() {
        val players = nCopies(6, PlayerCharacter("a", 3))
        assertEquals((125 * 0.5).toInt(), Encounter(players, nCopies(1, mock<MonsterInfo> { on { xp } doReturn 125 })).adjustedXp)
    }

    @Test
    fun encounterDifficulty_dmgExample() {
        val players = listOf(
                PlayerCharacter("a", 3),
                PlayerCharacter("b", 3),
                PlayerCharacter("c", 3),
                PlayerCharacter("d", 2))

        val monsters = listOf(
                mock<MonsterInfo> { on { xp } doReturn 200 }, // bugbear
                mock<MonsterInfo> { on { xp } doReturn 100 }, // hobgoblins
                mock<MonsterInfo> { on { xp } doReturn 100 },
                mock<MonsterInfo> { on { xp } doReturn 100 }
        )

        assertEquals(Hard, Encounter(players, monsters).encounterDifficulty)
    }
}
