from __future__ import annotations

from dataclasses import dataclass, field

from n2t.core.disassembler.entities import Address, Computation, Destination, Jump, Word


@dataclass
class DisassemblerChain:
    _next: DisassemblerChain = field(init=False)

    def disassemble(self, word: Word) -> str:
        try:
            return self._next.disassemble(word)
        except AttributeError:
            return f"// Disassembly of <{word}> failed."

    def __or__(self, other: DisassemblerChain) -> DisassemblerChain:
        try:
            self._next | other
        except AttributeError:
            self._next = other

        return self


@dataclass
class LengthValidator(DisassemblerChain):
    def disassemble(self, word: Word) -> str:
        if word.has_invalid_length():
            return f"// <{word}> has unacceptable length."

        return super().disassemble(word)


@dataclass
class AlphabetValidator(DisassemblerChain):
    def disassemble(self, word: Word) -> str:
        if word.violates_alphabet():
            return f"// <{word}> violates alphabet."

        return super().disassemble(word)


@dataclass
class AddressingDisassembler(DisassemblerChain):
    def disassemble(self, word: Word) -> str:
        if word.is_addressing():
            return str(Address(word))

        return super().disassemble(word)


@dataclass
class CommandDisassembler(DisassemblerChain):
    def disassemble(self, word: Word) -> str:
        if word.is_command():
            return f"{Destination(word)}{Computation(word)}{Jump(word)}"

        return super().disassemble(word)
