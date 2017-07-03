package org.flightofstairs.redesignedPotato

import android.app.Activity
import android.os.Bundle
import org.jetbrains.anko.*

private val LEFT = 123456
private val RIGHT = 123457

class MainActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = Layout()

        fragmentManager.beginTransaction()
                .add(LEFT, StatBlockFragment.forMonster("Mummy Lord"), "a")
                .add(RIGHT, StatBlockFragment.forMonster("Bugbear Chief"), "b")
                .commit()
        layout.setContentView(this)
    }
}

class Layout: AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        linearLayout {
            frameLayout {
                id = LEFT
            }.lparams {
                weight = 0.5f
            }
            frameLayout {
                id = RIGHT
            }.lparams {
                weight = 0.5f
            }
        }
    }
}
