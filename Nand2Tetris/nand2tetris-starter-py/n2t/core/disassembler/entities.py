from __future__ import annotations

from dataclasses import dataclass
from typing import Collection, Optional


@dataclass
class Word:
    value: str

    accepted_length: int = 16
    alphabet: Collection[str] = ("0", "1")

    def is_addressing(self) -> bool:
        return self.is_valid() and self.value.startswith("0")

    def is_command(self) -> bool:
        return self.is_valid() and self.value.startswith("111")

    def is_valid(self) -> bool:
        return not (self.has_invalid_length() or self.violates_alphabet())

    def has_invalid_length(self) -> bool:
        return len(self.value) != self.accepted_length

    def violates_alphabet(self) -> bool:
        return any(bit not in self.alphabet for bit in self.value)

    def sub_bus(self, start: int, finish: Optional[int] = None) -> str:
        finish = finish or len(self.value)
        assert start >= 0 and finish >= 0, "Invalid sub bus range requested"

        return self.value[start:finish]

    def __str__(self) -> str:
        return self.value


@dataclass
class Address:
    word: Word

    SIGN = "@"

    @property
    def _assembly(self) -> str:
        address_bits = self.word.sub_bus(start=1)

        return str(int(address_bits, base=2))

    def __str__(self) -> str:
        return f"{self.SIGN}{self._assembly}"


@dataclass
class Destination:
    word: Word

    MAP = {
        "000": "",
        "001": "M=",
        "010": "D=",
        "011": "MD=",
        "100": "A=",
        "101": "AM=",
        "110": "AD=",
        "111": "AMD=",
    }
    NO_DESTINATION = ""

    @property
    def hack(self) -> str:
        return self.word.sub_bus(start=10, finish=13)

    @property
    def assembly(self) -> str:
        return self.MAP.get(self.hack, self.NO_DESTINATION)

    def __str__(self) -> str:
        return self.assembly


@dataclass
class Computation:
    word: Word

    MAP = {
        "0101010": "0",
        "0111111": "1",
        "0111010": "-1",
        "0001100": "D",
        "0110000": "A",
        "1110000": "M",
        "0001101": "!D",
        "0110001": "!A",
        "1110001": "!M",
        "0001111": "-D",
        "0110011": "-A",
        "1110011": "-M",
        "0011111": "D+1",
        "0110111": "A+1",
        "1110111": "M+1",
        "0001110": "D-1",
        "0110010": "A-1",
        "1110010": "M-1",
        "0000010": "D+A",
        "1000010": "D+M",
        "0010011": "D-A",
        "0000111": "A-D",
        "1010011": "D-M",
        "1000111": "M-D",
        "0000000": "D&A",
        "1000000": "D&M",
        "0010101": "D|A",
        "1010101": "D|M",
    }

    @property
    def hack(self) -> str:
        return self.word.sub_bus(start=3, finish=10)

    @property
    def assembly(self) -> str:
        return self.MAP.get(self.hack, self.hack)

    def __str__(self) -> str:
        return self.assembly


@dataclass
class Jump:
    word: Word

    MAP = {
        "000": "",
        "001": ";JGT",
        "010": ";JEQ",
        "011": ";JGE",
        "100": ";JLT",
        "101": ";JNE",
        "110": ";JLE",
        "111": ";JMP",
    }
    NO_JUMP = ""

    @property
    def hack(self) -> str:
        return self.word.sub_bus(start=13)

    @property
    def assembly(self) -> str:
        return self.MAP.get(self.hack, self.NO_JUMP)

    def __str__(self) -> str:
        return self.assembly
