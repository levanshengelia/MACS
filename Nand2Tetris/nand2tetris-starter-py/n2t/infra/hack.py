from __future__ import annotations

from dataclasses import dataclass, field
from pathlib import Path
from typing import Iterable, Iterator, Protocol

from n2t.core import Disassembler as DefaultDisassembler
from n2t.infra.io import File, FileFormat


@dataclass
class HackProgram:
    path: Path
    disassembler: Disassembler = field(default_factory=DefaultDisassembler.create)

    def __post_init__(self) -> None:
        FileFormat.hack.validate(self.path)

    @classmethod
    def load_from(cls, file_name: str) -> HackProgram:
        return cls(Path(file_name))

    def disassemble(self) -> None:
        assembly_file = File(FileFormat.asm.convert(self.path))
        assembly_file.save(self.disassembler.disassemble(self))

    def __iter__(self) -> Iterator[str]:
        yield from File(self.path).load()


class Disassembler(Protocol):  # pragma: no cover
    def disassemble(self, words: Iterable[str]) -> Iterable[str]:
        pass
