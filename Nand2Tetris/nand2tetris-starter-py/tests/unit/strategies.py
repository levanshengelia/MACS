from __future__ import annotations

from dataclasses import dataclass
from typing import Any, Callable

from hypothesis.strategies import (
    SearchStrategy,
    characters,
    composite,
    integers,
    sampled_from,
    text,
)

WORD_SIZE = 16
ALPHABET = ("0", "1")
MAX_ADDRESS = 32767


def hack_words(size: int = WORD_SIZE) -> SearchStrategy[str]:
    return text(
        alphabet=ALPHABET,
        min_size=size,
        max_size=size,
    )


def short_words() -> SearchStrategy[str]:
    return text(max_size=WORD_SIZE - 1)


def long_words() -> SearchStrategy[str]:
    return text(min_size=WORD_SIZE + 1)


def gibberish_words() -> SearchStrategy[str]:
    return text(
        alphabet=characters(blacklist_characters=ALPHABET),
        max_size=WORD_SIZE,
        min_size=WORD_SIZE,
    )


@composite
def a_instructions(draw: Callable[..., Any]) -> HackAssemblyPair:
    address = draw(integers(min_value=0, max_value=MAX_ADDRESS))

    return HackAssemblyPair(
        hack=f"{address:016b}",
        assembly=f"@{address}",
    )


@dataclass(frozen=True)
class HackAssemblyPair:
    hack: str
    assembly: str


@composite
def c_instructions(draw: Callable[..., Any]) -> HackAssemblyPair:
    destination = draw(destinations())
    computation = draw(computations())
    jump = draw(jumps())

    return HackAssemblyPair(
        hack=f"111{computation.hack}{destination.hack}{jump.hack}",
        assembly=f"{destination.assembly}{computation.assembly}{jump.assembly}",
    )


def destinations() -> SearchStrategy[HackAssemblyPair]:
    return sampled_from(
        [
            HackAssemblyPair(hack="000", assembly=""),
            HackAssemblyPair(hack="001", assembly="M="),
            HackAssemblyPair(hack="010", assembly="D="),
            HackAssemblyPair(hack="011", assembly="MD="),
            HackAssemblyPair(hack="100", assembly="A="),
            HackAssemblyPair(hack="101", assembly="AM="),
            HackAssemblyPair(hack="110", assembly="AD="),
            HackAssemblyPair(hack="111", assembly="AMD="),
        ]
    )


def computations() -> SearchStrategy[HackAssemblyPair]:
    return sampled_from(
        [
            HackAssemblyPair(hack="0101010", assembly="0"),
            HackAssemblyPair(hack="0111111", assembly="1"),
            HackAssemblyPair(hack="0111010", assembly="-1"),
            HackAssemblyPair(hack="0001100", assembly="D"),
            HackAssemblyPair(hack="0110000", assembly="A"),
            HackAssemblyPair(hack="1110000", assembly="M"),
            HackAssemblyPair(hack="0001101", assembly="!D"),
            HackAssemblyPair(hack="0110001", assembly="!A"),
            HackAssemblyPair(hack="1110001", assembly="!M"),
            HackAssemblyPair(hack="0001111", assembly="-D"),
            HackAssemblyPair(hack="0110011", assembly="-A"),
            HackAssemblyPair(hack="1110011", assembly="-M"),
            HackAssemblyPair(hack="0011111", assembly="D+1"),
            HackAssemblyPair(hack="0110111", assembly="A+1"),
            HackAssemblyPair(hack="1110111", assembly="M+1"),
            HackAssemblyPair(hack="0001110", assembly="D-1"),
            HackAssemblyPair(hack="0110010", assembly="A-1"),
            HackAssemblyPair(hack="1110010", assembly="M-1"),
            HackAssemblyPair(hack="0000010", assembly="D+A"),
            HackAssemblyPair(hack="1000010", assembly="D+M"),
            HackAssemblyPair(hack="0010011", assembly="D-A"),
            HackAssemblyPair(hack="0000111", assembly="A-D"),
            HackAssemblyPair(hack="1010011", assembly="D-M"),
            HackAssemblyPair(hack="1000111", assembly="M-D"),
            HackAssemblyPair(hack="0000000", assembly="D&A"),
            HackAssemblyPair(hack="1000000", assembly="D&M"),
            HackAssemblyPair(hack="0010101", assembly="D|A"),
            HackAssemblyPair(hack="1010101", assembly="D|M"),
        ]
    )


def jumps() -> SearchStrategy[HackAssemblyPair]:
    return sampled_from(
        [
            HackAssemblyPair(hack="000", assembly=""),
            HackAssemblyPair(hack="001", assembly=";JGT"),
            HackAssemblyPair(hack="010", assembly=";JEQ"),
            HackAssemblyPair(hack="011", assembly=";JGE"),
            HackAssemblyPair(hack="100", assembly=";JLT"),
            HackAssemblyPair(hack="101", assembly=";JNE"),
            HackAssemblyPair(hack="110", assembly=";JLE"),
            HackAssemblyPair(hack="111", assembly=";JMP"),
        ]
    )
