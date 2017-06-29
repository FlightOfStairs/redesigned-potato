package org.flightofstairs.redesignedPotato

import org.flightofstairs.redesignedPotato.parsers.json.monstersFromResource

class Main {

}

fun main(args: Array<String>) {
    printMonsters("/monsters/Out_of_the_Abyss.json")
    printMonsters("/monsters/Monster_Manual.json")
}

private fun printMonsters(file: String) {
    val monsters = monstersFromResource(file)

    println(monsters.map { it.alignment }.toSet())
}

