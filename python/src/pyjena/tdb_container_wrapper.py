from pathlib import Path
from pyjena.gateway_client import GatewayClient
from pyjena.sparql_result import SparqlResult
from typing import Self
from types import TracebackType
from py4j.java_gateway import JavaObject


class TdbContainerWrapper:
    def __init__(self, client: GatewayClient | None = None) -> None:
        self.client = client or GatewayClient()
        self.app: JavaObject | None = None

    def __enter__(self) -> Self:
        self.app = self.client.start()
        return self

    def __exit__(
        self,
        exc_type: type[BaseException] | None,
        exc_val: BaseException | None,
        exc_tb: TracebackType | None,
    ) -> None:
        _ = exc_type
        _ = exc_val
        _ = exc_tb
        self.client.stop()

    def _app_if_not_none(self) -> JavaObject:
        if not self.app:
            raise RuntimeError(
                "No pyr4j server running. Access method inside a context"
            )
        return self.app

    def load(self, files: list[Path]) -> None:
        self._app_if_not_none().load([str(f) for f in files], True)

    def exec_select(self, query: str) -> SparqlResult:
        result = self._app_if_not_none().execSelect(query)
        return SparqlResult.from_java_object(result)

    def exec_update(self, query: str) -> None:
        self._app_if_not_none().execUpdate(query)
