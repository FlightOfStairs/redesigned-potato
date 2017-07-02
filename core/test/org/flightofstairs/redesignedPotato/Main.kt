package org.flightofstairs.redesignedPotato

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.flightofstairs.redesignedPotato.model.MonsterInfo
import org.flightofstairs.redesignedPotato.parsers.json.monstersFromResource


fun main(args: Array<String>) {
    val monsters = listOf<MonsterInfo>()
            .plus(monstersFromResource("/monsters/Out_of_the_Abyss.json"))
            .plus(monstersFromResource("/monsters/Monster_Manual.json"))

    println(jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(monsters.find { it.name == "Yeti" }))

    println(monsters.flatMap { it.conditionImmune }.toSet())

}
