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
    fun getAttributeOfType() {
        assertEquals(Attribute(19), ankylosaurus.getAttributeOfType(AttributeType.Str))
        assertEquals(Attribute(11), ankylosaurus.getAttributeOfType(AttributeType.Dex))
        assertEquals(Attribute(15), ankylosaurus.getAttributeOfType(AttributeType.Con))
        assertEquals(Attribute(2), ankylosaurus.getAttributeOfType(AttributeType.Int))
        assertEquals(Attribute(12), ankylosaurus.getAttributeOfType(AttributeType.Wis))
        assertEquals(Attribute(5), ankylosaurus.getAttributeOfType(AttributeType.Cha))
    }
}
