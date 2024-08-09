package com.github.davidkleiven.pyjena
import org.apache.jena.query.QueryExecutionFactory
import org.apache.jena.query.QueryFactory
import org.apache.jena.query.QuerySolution
import org.apache.jena.query.ReadWrite
import org.apache.jena.query.ResultSet
import org.apache.jena.sparql.core.DatasetGraph
import org.apache.jena.tdb2.TDB2Factory
import org.apache.jena.tdb2.loader.Loader

class TdbContainer {
    private val dataset: DatasetGraph = TDB2Factory.createDataset().asDatasetGraph()

    fun load(
        dataURLs: List<String>,
        showProgress: Boolean = false,
    ) {
        Loader.load(dataset, dataURLs, showProgress)
    }

    fun execSelect(queryString: String): SparqlResult {
        val query = QueryFactory.create(queryString)
        val execution = QueryExecutionFactory.create(query, dataset)

        dataset.begin(ReadWrite.READ)
        try {
            val resultSet = execution.execSelect()
            val head = Head(resultSet.resultVars)
            val bindings =
                generateSequence {
                    querySolutionOrNull(resultSet)
                }.map { solution ->
                    val varName = solution.varNames()
                    generateSequence { nextVariableOrNull(varName) }.associateWith { name ->
                        varToValue(solution, name)
                    }
                }.toList()
            return SparqlResult(head, Result(bindings))
        } finally {
            dataset.end()
        }
    }
}

fun querySolutionOrNull(resultSet: ResultSet): QuerySolution? {
    return try {
        resultSet.next()
    } catch (exc: Exception) {
        null
    }
}

fun nextVariableOrNull(iterator: Iterator<String>): String? {
    return try {
        iterator.next()
    } catch (exc: Exception) {
        null
    }
}

fun varToValue(
    solution: QuerySolution,
    varName: String,
): Value {
    val rdfNode = solution.get(varName)
    return if (rdfNode.isResource) {
        val resource = rdfNode.asResource()
        Value("uri", resource.toString())
    } else {
        val literal = rdfNode.asLiteral()
        Value("literal", literal.string, literal.datatypeURI)
    }
}
