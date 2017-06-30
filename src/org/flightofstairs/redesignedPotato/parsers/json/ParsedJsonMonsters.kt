package org.flightofstairs.redesignedPotato.parsers.json

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.flightofstairs.redesignedPotato.Main
import org.flightofstairs.redesignedPotato.model.*
import org.flightofstairs.redesignedPotato.model.AttackType.*
import org.flightofstairs.redesignedPotato.model.StandOff.Melee
import org.flightofstairs.redesignedPotato.model.StandOff.Ranged

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
                                 val selected: Boolean, // always false?
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
        val isWeapon = meleeWeaponAttack || rangedWeaponAttack
        val isSpell = meleeSpellAttack || rangedSpellAttack

        assert(!isWeapon || !isSpell)

        val attackType = if (isWeapon) Weapon else if (isSpell) Spell else Unknown

        val standOffs = setOf<StandOff>()
                .plus(if (meleeWeaponAttack || meleeSpellAttack) listOf(Melee) else listOf())
                .plus(if (rangedWeaponAttack || rangedSpellAttack) listOf(Ranged) else listOf())

        return MonsterAttack(type, Modifier(toHit), damages.map { it.toModel() }, attackType, standOffs, target, spellReach, spellRange, reach, range, maxRange)
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
    override fun toModel() = MonsterSources(book, page)
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
                                  val averageHitPoints: Int): Parsed<MonsterInfo> {
    override fun toModel(): MonsterInfo {
        return MonsterInfo(
                name,
                type(),
                size,
                alignment,
                ac(),
                hitPointsExpression(),
                speed,
                Attributes(Attribute(strength), Attribute(dexterity), Attribute(constitution), Attribute(intelligence), Attribute(wisdom), Attribute(charisma)),
                saves(),
                skills.map { it.toModel() },
                resist,
                immune,
                condtionImmune?.split(',')?.map { Condition.valueOf(it.trim()) }?.toSet() ?: setOf<Condition>(),
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

    private fun hitPointsExpression(): DiceExpression {
        val hitpointsExpression = DiceExpression.fromString(hitPoints)
        assert(hitpointsExpression.average().toInt() == averageHitPoints)
        return hitpointsExpression
    }

    private fun ac(): ArmourClass {
        val match = Regex("(\\d+)\\s*(?:\\(\\s*(.+)\\s*\\))?\\s*").matchEntire(armorClass) ?: throw RuntimeException("Weird looking armor: $armorClass")
        val (value, description) = match.destructured
        return ArmourClass(value.toInt(), if (description.isBlank()) null else description)
    }

    private fun type(): MonsterType {
        val match = Regex("(.+?)\\s*(?:\\(\\s*(.+?)\\s*\\)?\\s*)?").matchEntire(type) ?: throw RuntimeException("Weird looking type: $type")
        val (supertype, subtype) = match.destructured

        val monsterClass = MonsterClass.values().first {
            it.name.toLowerCase().replace('_', ' ') == supertype.toLowerCase()
        }

        return MonsterType( monsterClass, if (subtype.isBlank()) null else subtype)
    }


    private fun saves(): List<ExplicitSave> {
        val saves = (save ?: "").split(',').filter { it.isNotBlank() }.map {
            val (attribute, modifier) = it.trim().split(' ')
            ExplicitSave(AttributeType.valueOf(attribute), Modifier(modifier.toInt()))
        }
        return saves
    }
}

fun monstersFromResource(file: String): List<MonsterInfo> {
    val jsonStream = Main::class.java.getResourceAsStream(file)

    val objectMapper = jacksonObjectMapper()

    return objectMapper.readValue<List<ParsedMonster>>(jsonStream, object : TypeReference<List<ParsedMonster>>() {}).map { it.toModel() }
}

