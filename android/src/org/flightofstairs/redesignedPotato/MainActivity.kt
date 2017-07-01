package org.flightofstairs.redesignedPotato

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.flightofstairs.redesignedPotato.model.MonsterInfo
import org.flightofstairs.redesignedPotato.parsers.json.monstersFromResource
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val goblin = monstersFromResource("/monsters/Monster_Manual.json").find { it.name == "Goblin" }!!

        MonsterStatBlockView(goblin).setContentView(this)
    }
}

class MonsterStatBlockView(private val monster: MonsterInfo): AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        verticalLayout {
            textView(monster.name)
            textView("${monster.size} ${monster.type}, ${monster.alignment}")
            button("Hello world") {
                onClick {
                    toast("clicked")
                }
            }
        }
    }
}
