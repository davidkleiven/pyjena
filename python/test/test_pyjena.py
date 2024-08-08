from pathlib import Path
from pyjena.tdb_container_wrapper import TdbContainerWrapper

TEST_RESOURCES = Path(__file__).parent.parent.parent / "kotlin/src/test/resources"


def test_load_example1():
    data_file = TEST_RESOURCES / "example1.rdf"
    wrapper = TdbContainerWrapper()
    wrapper.load([data_file])
    res = wrapper.exec_select("select (count(*) as ?cnt) where {?s ?p ?o}")

    assert res.getResult().getBindings()[0]["cnt"].getValue() == "4"
