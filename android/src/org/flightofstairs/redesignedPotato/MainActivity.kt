package org.flightofstairs.redesignedPotato

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.Html.fromHtml
import android.view.ViewGroup
import org.flightofstairs.redesignedPotato.model.*
import org.flightofstairs.redesignedPotato.view.Text
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class MainActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MonsterStatBlockView(monsters["Mummy Lord"]!!).setContentView(this)
    }
}

class MonsterStatBlockView(private val monster: MonsterInfo): AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
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
                    monster.actions.forEach { action(it, ui.ctx) }
                }

                if (monster.legendaries.isNotEmpty()) {
                    divider()

                    textView("Legendary Actions")
                    monster.legendaries.forEach { trait(it) }
                }

            }
        }
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
        val html = if (VERSION.SDK_INT >= VERSION_CODES.N) fromHtml(text, FROM_HTML_MODE_LEGACY) else fromHtml(text)
        textView(html)
    }
}

