from pathlib import Path
from pyjena.gateway_client import GatewayClient


class TdbContainerWrapper:
    def __init__(self, client: GatewayClient | None = None) -> None:
        self.client = client or GatewayClient()

    def load(self, files: list[Path]) -> None:
        self.client.app.load([str(f) for f in files], True)

    def exec_select(self, query: str):
        return self.client.app.execSelect(query)
