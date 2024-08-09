from dataclasses import dataclass
from typing import Generator, Self
from py4j.java_gateway import JavaObject
from py4j.java_collections import JavaList


@dataclass
class Head:
    variables: list[str]


@dataclass
class Value:
    type: str
    value: str
    datatype: str | None = None


@dataclass
class Result:
    bindings: Generator[dict[str, Value], None, None]


@dataclass
class SparqlResult:
    head: Head
    result: Result

    @classmethod
    def from_java_object(cls, obj: JavaObject) -> Self:
        head = Head(obj.getHead().getVars())
        result = Result(bindings_genarator(obj.getResult().getBindings()))
        return cls(head, result)


def bindings_genarator(obj: JavaList) -> Generator[dict[str, Value], None, None]:
    for row in obj:
        yield {
            k: Value(v.getType(), v.getValue(), v.getDatatype()) for k, v in row.items()
        }
