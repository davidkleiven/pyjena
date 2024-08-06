package com.github.davidkleiven.pyjena.test
import com.github.davidkleiven.pyjena.test.tutils.InitializedTdbContainerFactory
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
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
    fun `test example1 has 4 triples`() {
        val container = InitializedTdbContainerFactory().example1()
        val res = container.execSelect("select * where {?s ?p ?o}")
        assertEquals(4, res.result.bindings.size)
    }

    @TestFactory
    fun `test triple extraction`() =
        listOf(InitializedTdbContainerFactory().example1(), InitializedTdbContainerFactory().example1AndExample2()).map {
            DynamicTest.dynamicTest("Test triple extraction") {
                val res = it.execSelect("select * where {?s <http://example.org/stuff/1.0/fullName> ?name}")
                assertEquals(res.result.bindings.size, 1)
                assertEquals("Dave Beckett", res.result.bindings[0]["name"]!!.value)
            }
        }

    @Test
    fun `test example2 count is 1`() {
        val container = InitializedTdbContainerFactory().example2()
        val res = container.execSelect("select * where {?s ?p ?o}")
        assertEquals(1, res.result.bindings.size)
    }

    @Test
    fun `test example1 and example2 count is 5`() {
        val container = InitializedTdbContainerFactory().example1AndExample2()
        val res = container.execSelect("select * where {?s ?p ?o}")
        assertEquals(5, res.result.bindings.size)
    }
}
