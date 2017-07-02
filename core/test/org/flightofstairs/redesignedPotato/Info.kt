package org.flightofstairs.redesignedPotato

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.flightofstairs.redesignedPotato.model.MonsterInfo
import org.flightofstairs.redesignedPotato.parsers.json.monstersFromResource
import org.junit.Test


class Info {
    @Test
    fun info() {
        val monsters = listOf<MonsterInfo>()
                .plus(monstersFromResource("/monsters/Out_of_the_Abyss.json"))
                .plus(monstersFromResource("/monsters/Monster_Manual.json"))

        println(jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(monsters.find { it.name == "Yeti" }))

        println(monsters.forEach { monster -> monster.actions.forEach { action -> if (action.attacks.size > 1) println(monster.name) } })
        println(monsters.flatMap { it.actions }.filter { it.attacks.size > 1 }.flatMap { it.attacks }.map { it.type }.toSet())
    }
}
