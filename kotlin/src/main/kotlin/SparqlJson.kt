package com.github.davidkleiven.pyjena

data class Head(val vars: List<String>)

data class Value(val type: String, val value: String, val datatype: String? = null)

data class Result(val bindings: List<Map<String, Value>>)

data class SparqlResult(val head: Head, val result: Result)
