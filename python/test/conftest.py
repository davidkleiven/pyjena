import pytest
from pyjena.gateway_client import GatewayClient
from typing import Generator


@pytest.fixture
def client() -> Generator[GatewayClient, None, None]:
    c = GatewayClient()
    try:
        yield c
    finally:
        c.shutdown()
