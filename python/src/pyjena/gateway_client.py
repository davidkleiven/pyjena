from pathlib import Path
from subprocess import Popen, PIPE
import logging
from py4j.java_gateway import JavaGateway, GatewayParameters, JavaObject
import time

logger = logging.getLogger(__name__)


def start_service(max_startup_time_sec: int, java_cmd: str) -> Popen:
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


def create_fresh_gateway() -> JavaGateway:
    gateway = create_gateway()
    gateway.shutdown()  # In case it was running an old instance
    return create_gateway()


class GatewayClient:
    def __init__(self, max_startup_time_sec: int = 1, java_cmd: str = "java") -> None:
        self.gateway = create_fresh_gateway()
        self.proc = start_service(max_startup_time_sec, java_cmd)

    @property
    def app(self) -> JavaObject:
        return self.gateway.entry_point

    def shutdown(self):
        try:
            logger.info("Shutting down pyjena-service")
            self.gateway.shutdown()
        except Exception as exc:
            logger.exception(exc)

    def __del__(self):
        self.shutdown()
