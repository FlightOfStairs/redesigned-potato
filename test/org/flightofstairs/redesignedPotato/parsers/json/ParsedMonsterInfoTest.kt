package org.flightofstairs.redesignedPotato.parsers.json

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Test
import org.skyscreamer.jsonassert.JSONAssert
import java.nio.charset.StandardCharsets.UTF_8

class ParsedMonsterInfoTest {
    @Test
    fun yeti() {
        val objectMapper = jacksonObjectMapper()
        val parsedMonster = objectMapper.readValue(javaClass.getResourceAsStream("/yeti-raw.json"), ParsedMonster::class.java)
        val modelledMonsterJson = objectMapper.writeValueAsString(parsedMonster.toModel())

        val expectedYetiJson = javaClass.getResourceAsStream("/yeti-modelled.json").reader(UTF_8).readText()

        JSONAssert.assertEquals(expectedYetiJson, modelledMonsterJson, true)
    }
}
