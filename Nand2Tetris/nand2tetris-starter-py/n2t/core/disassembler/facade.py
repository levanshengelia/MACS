from __future__ import annotations

from dataclasses import dataclass
from typing import Iterable

from n2t.core.disassembler.chain import (
    AddressingDisassembler,
    AlphabetValidator,
    CommandDisassembler,
    DisassemblerChain,
    LengthValidator,
)
from n2t.core.disassembler.entities import Word


@dataclass
class Disassembler:
    chain: DisassemblerChain

    @classmethod
    def create(cls) -> Disassembler:
        return cls(
            LengthValidator()
            | AlphabetValidator()
            | AddressingDisassembler()
            | CommandDisassembler()
        )

    def disassemble(self, words: Iterable[str]) -> Iterable[str]:
        for word in words:
            yield self.disassemble_one(word)

    def disassemble_one(self, word: str) -> str:
        return self.chain.disassemble(Word(word))
