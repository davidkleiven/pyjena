package com.github.davidkleiven.pyjena.test.tutils

import com.github.davidkleiven.pyjena.TdbContainer
import kotlin.io.path.writeText

class InitializedTdbContainerFactory {
    private fun containerFromResource(resource: String): TdbContainer {
        val container = TdbContainer()
        val content = InitializedTdbContainerFactory::class.java.getResource(resource)?.readText()!!
        val file = kotlin.io.path.createTempFile("example1", ".rdf")
        file.writeText(content)
        container.load(listOf(file.toString()))
        return container
    }

    fun example1(): TdbContainer {
        return containerFromResource("/example1.rdf")
    }
}
