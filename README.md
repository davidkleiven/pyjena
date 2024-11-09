# PyJena

Simple python interface to parts of the SparQL features in Jena

## Insert and query data

```python
>>> from pyjena.tdb_container_wrapper import TdbContainerWrapper
>>> with TdbContainerWrapper() as wrapper:
...     wrapper.exec_update("INSERT DATA {_:b0 <http://name> \"John\"}")
...     sparql_result = wrapper.exec_select("SELECT * WHERE {?s ?p ?o}") # doctest: +ELLIPSIS
...     print([b["o"].value for b in sparql_result.result.bindings])
['John']

```