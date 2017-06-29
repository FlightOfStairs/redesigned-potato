package org.flightofstairs.redesignedPotato.parsers.json

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.flightofstairs.redesignedPotato.Main
import org.flightofstairs.redesignedPotato.model.*

internal interface Parsed<out Type> {
    fun toModel(): Type
}

internal data class ParsedSkill(val id: Int, val type: Skill, val modifier: Int): Parsed<ExplicitSkillModifier> {
    override fun toModel() = ExplicitSkillModifier(type, Modifier(modifier))
}

internal data class ParsedTrait(val id: Int, val name: String, val description: String): Parsed<MonsterTrait> {
    override fun toModel() = MonsterTrait(name, description)
}

internal data class ParsedAttackDamages(val id: Int,
                                        val roll: String,
                                        val type: String?,
                                        val average: Int,
                                        val special: String?): Parsed<MonsterAttackDamage> {

    override fun toModel() = MonsterAttackDamage(if (roll.isBlank()) null else DiceExpression.fromString(roll), type, special)
}

internal data class ParsedAttack(val id: Int,
                                 val type: String,
                                 val toHit: Int,
                                 val damages: List<ParsedAttackDamages>,
                                 val selected: Boolean,
                                 val meleeWeaponAttack: Boolean,
                                 val meleeSpellAttack: Boolean,
                                 val rangedWeaponAttack: Boolean,
                                 val rangedSpellAttack: Boolean,
                                 val target: String?,
                                 val spellReach: String?,
                                 val spellRange: String?,
                                 val reach: String?,
                                 val range: String?,
                                 val maxRange: String?): Parsed<MonsterAttack> {
    override fun toModel(): MonsterAttack {


        return MonsterAttack(type, Modifier(toHit), damages.map { it.toModel() }, selected, meleeWeaponAttack, meleeSpellAttack, rangedWeaponAttack, rangedSpellAttack, target, spellReach, spellRange, reach, range, maxRange)
    }
}

internal data class ParsedAction(val id: Int, val name: String, val description: String, val attacks: List<ParsedAttack>): Parsed<MonsterAction> {
    override fun toModel(): MonsterAction {
        return MonsterAction(name, description, attacks.map { it.toModel() })
    }
}

internal data class ParsedEnvironment(val id: Int, val name: MonsterEnvironment): Parsed<MonsterEnvironment> {
    override fun toModel() = name
}

internal data class ParsedSources(val id: Int, val book: String, val page: Int): Parsed<MonsterSources> {
    override fun toModel() = MonsterSources(id, book, page)
}

internal data class ParsedMonster(val id: Int,
                                  val name: String,
                                  val type: String,
                                  val size: MonsterSize,
                                  val alignment: String,
                                  val armorClass: String,
                                  val hitPoints: String,
                                  val speed: String,
                                  val strength: Int,
                                  val dexterity: Int,
                                  val constitution: Int,
                                  val intelligence: Int,
                                  val wisdom: Int,
                                  val charisma: Int,
                                  val save: String?,
                                  val skills: List<ParsedSkill>,
                                  val resist: String?,
                                  val immune: String?,
                                  val condtionImmune: String?, // misspelling in JSON
                                  val senses: String?,
                                  val passive: Int,
                                  val languages: String?,
                                  val challengeRating: String,
                                  val xp: Int,
                                  val traits: List<ParsedTrait>,
                                  val actions: List<ParsedAction>,
                                  val reactions: List<ParsedTrait>,
                                  val legendaries: List<ParsedTrait>,
                                  val environments: List<ParsedEnvironment>,
                                  val sources: List<ParsedSources>,
                                  val averageHitPoints: Int): Parsed<Monster> {
    override fun toModel(): Monster {
        val hitpointsExpression = DiceExpression.fromString(hitPoints)
        assert(hitpointsExpression.average().toInt() == averageHitPoints)

        return Monster(
                name,
                type,
                size,
                alignment,
                armorClass,
                hitpointsExpression,
                speed,
                Attributes(Attribute(strength), Attribute(dexterity), Attribute(constitution), Attribute(intelligence), Attribute(wisdom), Attribute(charisma)),
                save,
                skills.map { it.toModel() },
                resist,
                immune,
                condtionImmune,
                senses,
                passive,
                languages,
                challengeRating,
                xp,
                traits.map { it.toModel() },
                actions.map { it.toModel() },
                reactions.map { it.toModel() },
                legendaries.map { it.toModel() },
                environments.map { it.toModel() },
                sources.map { it.toModel() })
    }
}

fun monstersFromResource(file: String): List<Monster> {
    val jsonStream = Main::class.java.getResourceAsStream(file)

    val objectMapper = jacksonObjectMapper()

    return objectMapper.readValue<List<ParsedMonster>>(jsonStream, object : TypeReference<List<ParsedMonster>>() {}).map { it.toModel() }
}


