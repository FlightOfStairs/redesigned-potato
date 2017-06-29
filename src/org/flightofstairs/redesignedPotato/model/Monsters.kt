package org.flightofstairs.redesignedPotato.model


enum class MonsterSize { Tiny, Small, Medium, Large, Huge, Gargantuan, }

data class ExplicitSkillModifier(val skill: Skill, val modifier: Modifier)

data class MonsterTrait(val name: String, val description: String)

data class MonsterAttackDamage(val diceExpression: DiceExpression?,
                               val type: String?,
                               val special: String?)

data class MonsterAttack(val type: String,
                         val toHitModifier: Modifier,
                         val damages: List<MonsterAttackDamage>,
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
                         val maxRange: String?)

data class MonsterAction(val name: String, val description: String, val attacks: List<MonsterAttack>)

typealias MonsterEnvironment = String

data class MonsterSources(val id: Int, val book: String, val page: Int)

data class Monster(val name: String,
                   val type: String,
                   val size: MonsterSize,
                   val alignment: String,
                   val armorClass: String,
                   val hitPoints: DiceExpression,
                   val speed: String,
                   val attributes: Attributes,
                   val save: String?,
                   val skills: List<ExplicitSkillModifier>,
                   val resist: String?,
                   val immune: String?,
                   val condtionImmune: String?, // misspelling in JSON
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
