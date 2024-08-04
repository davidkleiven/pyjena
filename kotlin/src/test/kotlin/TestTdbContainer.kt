package com.github.davidkleiven.pyjena.test
import com.github.davidkleiven.pyjena.test.tutils.InitializedTdbContainerFactory
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestTdbContainer {
    @Test
    fun `test load xml example1 count is 4`() {
        val container = InitializedTdbContainerFactory().example1()
        val res = container.execSelect("select (count(*) as ?cnt) where {?s ?p ?o}")
        assertEquals(1, res.result.bindings.size)
        assertEquals("4", res.result.bindings[0]["cnt"]!!.value)
    }

    @Test
    fun `test triple extraction`() {
        val container = InitializedTdbContainerFactory().example1()
        val res = container.execSelect("select * where {?s <http://example.org/stuff/1.0/fullName> ?name}")
        assertEquals(res.result.bindings.size, 1)
        assertEquals("Dave Beckett", res.result.bindings[0]["name"]!!.value)
    }
}
