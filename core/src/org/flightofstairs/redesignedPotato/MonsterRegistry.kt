package org.flightofstairs.redesignedPotato

import org.flightofstairs.redesignedPotato.model.MonsterInfo
import org.flightofstairs.redesignedPotato.parsers.json.monstersFromResource

val monsters = listOf<MonsterInfo>()
        .plus(monstersFromResource("/monsters/Out_of_the_Abyss.json"))
        .plus(monstersFromResource("/monsters/Monster_Manual.json"))
        .associateBy { it.name }
