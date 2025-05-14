from __future__ import annotations

from dataclasses import dataclass, field
from pathlib import Path
from typing import Iterable, Iterator, Protocol

from n2t.core import Assembler as DefaultAssembler
from n2t.infra.io import File, FileFormat


@dataclass
class AsmProgram:
    path: Path
    assembler: Assembler = field(default_factory=DefaultAssembler.create)

    @classmethod
    def load_from(cls, file_name: str) -> AsmProgram:
        return cls(Path(file_name))

    def __post_init__(self) -> None:
        FileFormat.asm.validate(self.path)

    def assemble(self) -> None:
        hack_file = File(FileFormat.hack.convert(self.path))
        hack_file.save(self.assembler.assemble(self))

    def __iter__(self) -> Iterator[str]:
        yield from File(self.path).load()


class Assembler(Protocol):  # pragma: no cover
    def assemble(self, assembly: Iterable[str]) -> Iterable[str]:
        pass
