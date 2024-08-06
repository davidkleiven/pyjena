package com.github.davidkleiven.pyjena.test.tutils

import com.github.davidkleiven.pyjena.TdbContainer
import kotlin.io.path.writeText

class InitializedTdbContainerFactory {
    private fun loadFromResources(
        container: TdbContainer,
        resources: List<String>,
    ) {
        val files =
            resources.map {
                val content = InitializedTdbContainerFactory::class.java.getResource(it)?.readText()!!
                val prefix = it.split(".")[0].replace("/", "")
                val file = kotlin.io.path.createTempFile(prefix, ".rdf")
                file.writeText(content)
                file.toString()
            }.toList()

        container.load(files)
    }

    fun example1(): TdbContainer {
        val container = TdbContainer()
        loadFromResources(container, listOf("/example1.rdf"))
        return container
    }

    fun example2(): TdbContainer {
        val container = TdbContainer()
        loadFromResources(container, listOf("/example2.rdf"))
        return container
    }

    fun example1AndExample2(): TdbContainer {
        val container = TdbContainer()
        loadFromResources(container, listOf("/example1.rdf", "/example2.rdf"))
        return container
    }
}
