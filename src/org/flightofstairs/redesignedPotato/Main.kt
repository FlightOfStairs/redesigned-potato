package org.flightofstairs.redesignedPotato

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.flightofstairs.redesignedPotato.model.Monster
import org.flightofstairs.redesignedPotato.parsers.json.monstersFromResource

class Main {

}


fun main(args: Array<String>) {
    val monsters = listOf<Monster>()
            .plus(monstersFromResource("/monsters/Out_of_the_Abyss.json"))
            .plus(monstersFromResource("/monsters/Monster_Manual.json"))

    println(jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(monsters.find { it.name == "Yeti" }))

    monsters.flatMap { it.actions}.flatMap { it.attacks }.forEach { if (it.standOffs.size > 1) println(it.damages) }

}
