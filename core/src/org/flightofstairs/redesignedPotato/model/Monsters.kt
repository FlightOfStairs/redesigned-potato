package org.flightofstairs.redesignedPotato.model


enum class MonsterSize { Tiny, Small, Medium, Large, Huge, Gargantuan, }

data class ExplicitSkillModifier(val skill: Skill, val modifier: Modifier)

data class MonsterTrait(val name: String, val description: String)

data class MonsterAttackDamage(val diceExpression: DiceExpression?,
                               val type: String?,
                               val special: String?)

enum class AttackType { Weapon, Spell, Unknown, }
enum class StandOff { Melee, Ranged, }

data class MonsterAttack(val type: String,
                         val toHitModifier: Modifier,
                         val damages: List<MonsterAttackDamage>,
                         val attackType: AttackType,
                         val standOffs: Set<StandOff>,
                         val target: String?,
                         val spellReach: String?,
                         val spellRange: String?,
                         val reach: String?,
                         val range: String?,
                         val maxRange: String?)

data class MonsterAction(val name: String, val description: String, val attacks: List<MonsterAttack>)

typealias MonsterEnvironment = String

data class MonsterSources(val book: String, val page: Int)

data class ArmourClass(val value: Int, val description: String?)

enum class MonsterClass { aberration, humanoid, plant, beast, monstrosity, fiend, dragon, elemental, construct, undead, fey, giant, ooze, celestial, swarm_of_tiny_beasts, }

data class MonsterType(val monsterClass: MonsterClass, val subtype: String?) {
    override fun toString() = when (this.subtype) {
        null -> "$monsterClass"
        else -> "$monsterClass ($subtype)"
    }
}

enum class Condition {
    blinded,
    charmed,
    deafened,
    exhaustion,
    frightened,
    grappled,
    necrotic,
    paralyzed,
    petrified,
    poisoned,
    prone,
    restrained,
    stunned,
    unconscious,
}

data class MonsterInfo(val name: String,
                       val type: MonsterType,
                       val size: MonsterSize,
                       val alignment: String,
                       val armorClass: ArmourClass,
                       val hitPoints: DiceExpression,
                       val speed: String,
                       val attributes: Attributes,
                       val saves: List<ExplicitSave>,
                       val skills: List<ExplicitSkillModifier>,
                       val resist: String?,
                       val immune: String?,
                       val conditionImmune: Set<Condition>,
                       val senses: String?,
                       val passive: Int,
                       val languages: String?,
                       val challengeRating: String,
                       val xp: Int,
                       val traits: List<MonsterTrait>,
                       val actions: List<MonsterAction>,
                       val reactions: List<MonsterTrait>,
                       val legendaries: List<MonsterTrait>,
                       val environments: List<MonsterEnvironment>,
                       val sources: List<MonsterSources>)
