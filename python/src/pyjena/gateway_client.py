from __future__ import annotations
from pathlib import Path
from subprocess import Popen, PIPE
import logging
from py4j.java_gateway import JavaGateway, GatewayParameters, JavaObject
import time
from types import TracebackType

logger = logging.getLogger(__name__)


def start_service(max_startup_time_sec: int, java_cmd: str) -> Popen[bytes]:
    pyjena_service = Path(__file__).parent / "backend/pyjena-service.jar"
    logger.info("Launching the pyjena-service")
    proc = Popen([java_cmd, "-jar", pyjena_service], stdin=PIPE)

    # TODO: Make something more solid. Perhaps let the suprocess write something
    # to a file when it is started
    time.sleep(max_startup_time_sec)
    return proc


def create_gateway() -> JavaGateway:
    return JavaGateway(
        gateway_parameters=GatewayParameters(auto_convert=True, auto_field=True)
    )


class GatewayClient:
    def __init__(self, max_startup_time_sec: int = 1, java_cmd: str = "java") -> None:
        self.gateway = None
        self.proc = None
        self.max_startupe_time_sec = max_startup_time_sec
        self.java_cmd = java_cmd

    def start(self) -> JavaObject:
        self.gateway = create_gateway()
        self.proc = start_service(self.max_startupe_time_sec, self.java_cmd)
        return self.gateway.entry_point

    def stop(self) -> None:
        if self.gateway:
            self.gateway.shutdown()

    def __enter__(self) -> JavaObject:
        return self.start()

    def __exit__(
        self,
        exc_type: type[BaseException] | None,
        exc_val: BaseException | None,
        exc_tb: TracebackType | None,
    ) -> None:
        _ = exc_type
        _ = exc_val
        _ = exc_tb
        self.stop()
