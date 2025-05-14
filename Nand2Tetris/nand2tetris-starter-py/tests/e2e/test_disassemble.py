import filecmp
from pathlib import Path

import pytest

from n2t.runner.cli import run_disassembler

_TEST_PROGRAMS = ["empty", "wrong", "add", "max", "rect", "pong"]

@pytest.mark.skip
@pytest.mark.parametrize("program", _TEST_PROGRAMS)
def test_should_disassemble(program: str, hack_directory: Path) -> None:
    hack_file = str(hack_directory.joinpath(f"{program}.hack"))

    run_disassembler(hack_file)

    assert filecmp.cmp(
        shallow=False,
        f1=str(hack_directory.joinpath(f"{program}.cmp")),
        f2=str(hack_directory.joinpath(f"{program}.asm")),
    )
