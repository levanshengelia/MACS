from __future__ import annotations

from dataclasses import dataclass
from typing import Iterable

from n2t.core.assembler import assembler_helper


@dataclass
class Assembler:
    @classmethod
    def create(cls) -> Assembler:
        return cls()

    def assemble(self, assembly) -> Iterable[str]:
        lines = assembler_helper.get_file_content(assembly)
        symbols = assembler_helper.get_predefined_symbols()
        labels = assembler_helper.get_labels(lines)

        symbols_and_labels: dict[str, str] = symbols.copy()
        symbols_and_labels.update(labels)

        result = assembler_helper.convert_assembly_to_binary(lines, symbols_and_labels)
        return result



