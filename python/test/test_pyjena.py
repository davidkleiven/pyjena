from pathlib import Path
from pyjena.tdb_container_wrapper import TdbContainerWrapper
from pyjena.gateway_client import GatewayClient

TEST_RESOURCES = Path(__file__).parent.parent.parent / "kotlin/src/test/resources"


def test_load_example1(client: GatewayClient):
    data_file = TEST_RESOURCES / "example1.rdf"
    wrapper = TdbContainerWrapper(client)
    wrapper.load([data_file])
    res = wrapper.exec_select("select (count(*) as ?cnt) where {?s ?p ?o}")

    assert list(res.result.bindings)[0]["cnt"].value == "4"
