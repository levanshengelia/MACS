from dataclasses import dataclass
from enum import Enum

class Kind(Enum):
    STATIC = 1,
    FIELD = 2,
    ARG = 3,
    VAR = 4,
    CLASS_NAME = 5,
    METHOD_NAME = 6,

@dataclass
class Symbol:
    type: str
    kind: Kind
    index: int


class SymbolTable:
    def __init__(self):
        self.class_scope: dict[str, Symbol] = {}
        self.subroutine_scope: dict[str, Symbol] = {}
        self.index_counters: dict[Kind, int] = {
            Kind.STATIC: 0,
            Kind.FIELD: 0,
            Kind.ARG: 0,
            Kind.VAR: 0
        }

    def start_subroutine(self):
        self.subroutine_scope.clear()
        self.index_counters[Kind.ARG] = 0
        self.index_counters[Kind.VAR] = 0

    def define(self, name: str, type_: str, kind: Kind, is_method: bool):
        if kind in (Kind.STATIC, Kind.FIELD):
            table = self.class_scope
        elif kind in (Kind.ARG, Kind.VAR):
            table = self.subroutine_scope
        else:
            raise ValueError(f"Unknown kind: {kind}")

        if is_method and kind == Kind.ARG and self.index_counters[Kind.ARG] == 0:
            self.index_counters[Kind.ARG] = 1

        index = self.index_counters[kind]
        table[name] = Symbol(type=type_, kind=kind, index=index)
        self.index_counters[kind] += 1

    def var_count(self, kind: Kind) -> int:
        return self.index_counters.get(kind, 0)

    def kind_of(self, name: str) -> Kind | None:
        symbol = self._find_symbol(name)
        return symbol.kind if symbol else None

    def type_of(self, name: str) -> str | None:
        symbol = self._find_symbol(name)
        return symbol.type if symbol else None

    def index_of(self, name: str) -> int | None:
        symbol = self._find_symbol(name)
        return symbol.index if symbol else None

    def _find_symbol(self, name: str) -> Symbol | None:
        return self.subroutine_scope.get(name) or self.class_scope.get(name)
