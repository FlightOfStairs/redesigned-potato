package org.flightofstairs.redesignedPotato.model

enum class Skill(val baseAttributeType: AttributeType) {
    Athletics(AttributeType.Str),

    Acrobatics(AttributeType.Dex),
    Slight_of_hand(AttributeType.Dex),
    Stealth(AttributeType.Dex),

    Arcana(AttributeType.Int),
    History(AttributeType.Int),
    Investigation(AttributeType.Int),
    Nature(AttributeType.Int),
    Religion(AttributeType.Int),

    Animal_handling(AttributeType.Wis),
    Insight(AttributeType.Wis),
    Medicine(AttributeType.Wis),
    Perception(AttributeType.Wis),
    Survival(AttributeType.Wis),

    Deception(AttributeType.Cha),
    Intimidation(AttributeType.Cha),
    Performance(AttributeType.Cha),
    Persuasion(AttributeType.Cha),
}

enum class AttributeType { Str, Dex, Con, Int, Wis, Cha, }

data class Modifier(val number: Int) {
    override fun toString() = if (number >= 0) "+$number" else "-$number"
}

data class Attribute(val score: Int) {
    val modifier = Modifier(score / 2 - 5)
}

data class Attributes(val str: Attribute, val dex: Attribute, val con: Attribute, val int: Attribute, val wis: Attribute, val cha: Attribute) {
    fun getAttributeOfType(attributeType: AttributeType) = when (attributeType) {
        AttributeType.Str -> str
        AttributeType.Dex -> dex
        AttributeType.Con -> con
        AttributeType.Int -> int
        AttributeType.Wis -> wis
        AttributeType.Cha -> cha
    }
}

data class ExplicitSave(val attributeType: AttributeType, val modifier: Modifier)
