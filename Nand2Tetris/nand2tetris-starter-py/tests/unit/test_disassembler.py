from __future__ import annotations

from hypothesis import given
from hypothesis.strategies import one_of

from n2t.core.disassembler import Disassembler
from tests.unit.strategies import (
    HackAssemblyPair,
    a_instructions,
    c_instructions,
    gibberish_words,
    hack_words,
    long_words,
    short_words,
)


@given(instruction=one_of(short_words(), long_words()))
def test_should_comment_unacceptable_length(instruction: str) -> None:
    disassembler = Disassembler.create()

    assembly = disassembler.disassemble_one(word=instruction)

    assert assembly == f"// <{instruction}> has unacceptable length."


@given(gibberish=gibberish_words())
def test_should_comment_alphabet_violation(gibberish: str) -> None:
    disassembler = Disassembler.create()

    assembly = disassembler.disassemble_one(word=gibberish)

    assert assembly == f"// <{gibberish}> violates alphabet."


@given(instruction=one_of(a_instructions(), c_instructions()))
def test_should_disassemble(instruction: HackAssemblyPair) -> None:
    disassembler = Disassembler.create()

    assembly = disassembler.disassemble_one(word=instruction.hack)

    assert assembly == instruction.assembly


@given(hack_word=hack_words())
def test_should_not_fail_fuzzing(hack_word: str) -> None:
    disassembler = Disassembler.create()

    disassembler.disassemble_one(word=hack_word)
