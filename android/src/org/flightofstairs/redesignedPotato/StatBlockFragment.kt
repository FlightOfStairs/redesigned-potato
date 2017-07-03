package org.flightofstairs.redesignedPotato

import android.app.Fragment
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.flightofstairs.redesignedPotato.model.AttributeType
import org.flightofstairs.redesignedPotato.model.Attributes
import org.flightofstairs.redesignedPotato.model.MonsterAction
import org.flightofstairs.redesignedPotato.model.MonsterTrait
import org.flightofstairs.redesignedPotato.view.Text
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.slf4j.LoggerFactory

private val MONSTER_NAME_ARGUMENT = "monsterName"

class StatBlockFragment: Fragment() {
    companion object {
        fun forMonster(monsterName: String) = StatBlockFragment().apply {
            arguments = Bundle().apply {
                putString(MONSTER_NAME_ARGUMENT, monsterName)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val monsterName = arguments[MONSTER_NAME_ARGUMENT]

        if (monsterName == null) {
            LoggerFactory.getLogger(StatBlockFragment::class.java).warn("Empty $MONSTER_NAME_ARGUMENT")
            return UI { }.view
        }

        val monster = monsters[monsterName]!! // todo failure handling

        return UI {
            scrollView {
                verticalLayout {
                    textView(monster.name)
                    textView("${monster.size} ${monster.type}, ${monster.alignment}")

                    divider()

                    property("Armor Class", monster.armorClass.toString())
                    property("Hit Points", "${monster.hitPoints.average()} (${monster.hitPoints})")

                    divider()

                    attributes(monster.attributes)

                    divider()

                    property("Saving Throws", monster.saves.map { "${it.attributeType} ${it.modifier}" })
                    property("Skills", monster.skills.map { "${it.skill} ${it.modifier}" })
                    property("Damage Resistances", monster.resist)
                    property("Damage Immunities", monster.immune)
                    property("Condition Immunities", monster.conditionImmune)
                    property("Senses", monster.senses)
                    property("Languages", monster.languages)
                    property("Challenge", "${monster.challengeRating} (${monster.xp} XP)")

                    divider()

                    monster.traits.forEach { trait(it) }

                    if (monster.actions.isNotEmpty()) {
                        divider()

                        textView("Actions")
                        monster.actions.forEach { action(it, context) }
                    }

                    if (monster.legendaries.isNotEmpty()) {
                        divider()

                        textView("Legendary Actions")
                        monster.legendaries.forEach { trait(it) }
                    }

                }
            }
        }.view
    }

    private fun ViewGroup.divider() {
        linearLayout {
            view {
                backgroundColor = Color.LTGRAY
            }.lparams {
                width = matchParent
                height = dip(5)
            }
        }
    }

    private fun ViewGroup.property(title: String, value: String?) {
        if (value == null) return
        htmlTextView("<b>$title</b> $value")
    }

    private fun <X> ViewGroup.property(title: String, values: Collection<X>) {
        if (values.isEmpty()) return
        property(title, values.joinToString())
    }

    private fun ViewGroup.attributes(attributes: Attributes) {
        linearLayout {
            AttributeType.values().forEach {
                verticalLayout {
                    textView(it.name.toUpperCase())
                    val attribute = attributes.getAttributeOfType(it)
                    textView("${attribute.score} (${attribute.modifier.number})")
                }
            }
        }
    }

    private fun ViewGroup.trait(trait: MonsterTrait) {
        htmlTextView("<b>${trait.name}</b> ${trait.description}")
    }

    private fun ViewGroup.action(action: MonsterAction, ctx: Context) {
        verticalLayout {
            htmlTextView("<b>${action.name}</b> ${action.description}")
            onClick {
                if (action.attacks.isNotEmpty()) {
                    val attacksText = Text.rollAttacks(action)
                    ctx.longToast(attacksText)
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    fun ViewGroup.htmlTextView(source: String) {
        val text = source.replace("\n", "<br />")
        val html = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(text)
        textView(html)
    }
}
