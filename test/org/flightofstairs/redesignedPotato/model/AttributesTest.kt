package org.flightofstairs.redesignedPotato.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AttributesTest {
    val ankylosaurus = Attributes(Attribute(19), Attribute(11), Attribute(15), Attribute(2), Attribute(12), Attribute(5))

    @Test
    fun skillModifiers() {
        assertEquals(Modifier(4), ankylosaurus.str.modifier)
        assertEquals(Modifier(0), ankylosaurus.dex.modifier)
        assertEquals(Modifier(2), ankylosaurus.con.modifier)
        assertEquals(Modifier(-4), ankylosaurus.int.modifier)
        assertEquals(Modifier(1), ankylosaurus.wis.modifier)
        assertEquals(Modifier(-3), ankylosaurus.cha.modifier)
    }

    @Test
    fun getAtributeOfType() {
        assertEquals(Attribute(19), ankylosaurus.getAtributeOfType(AttributeType.Str))
        assertEquals(Attribute(11), ankylosaurus.getAtributeOfType(AttributeType.Dex))
        assertEquals(Attribute(15), ankylosaurus.getAtributeOfType(AttributeType.Con))
        assertEquals(Attribute(2), ankylosaurus.getAtributeOfType(AttributeType.Int))
        assertEquals(Attribute(12), ankylosaurus.getAtributeOfType(AttributeType.Wis))
        assertEquals(Attribute(5), ankylosaurus.getAtributeOfType(AttributeType.Cha))
    }
}
