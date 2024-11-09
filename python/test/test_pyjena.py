from pathlib import Path
from pyjena.tdb_container_wrapper import TdbContainerWrapper

TEST_RESOURCES = Path(__file__).parent.parent.parent / "kotlin/src/test/resources"


def test_load_example1():
    data_file = TEST_RESOURCES / "example1.rdf"
    with TdbContainerWrapper() as wrapper:
        wrapper.load([data_file])
        res = wrapper.exec_select("select (count(*) as ?cnt) where {?s ?p ?o}")

        assert list(res.result.bindings)[0]["cnt"].value == "4"


def test_update():
    data_file = TEST_RESOURCES / "example1.rdf"
    with TdbContainerWrapper() as wrapper:
        wrapper.load([data_file])
        wrapper.exec_update('INSERT DATA {_:b0 <http://a> "b"}')
        res = wrapper.exec_select("select (count(*) as ?cnt) where {?s <http://a> ?o}")
        assert list(res.result.bindings)[0]["cnt"].value == "1"
