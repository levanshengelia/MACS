import filecmp
from itertools import zip_longest
from pathlib import Path

import pytest

from n2t.runner.cli import run_assembler

_TEST_PROGRAMS = ["empty", "addL", "maxL", "rectL", "pongL", "max", "rect", "pong"]


@pytest.mark.skip
@pytest.mark.parametrize("program", _TEST_PROGRAMS)
def test_should_assemble(program: str, asm_directory: Path) -> None:
    asm_file = str(asm_directory.joinpath(f"{program}.asm"))

    run_assembler(asm_file)

    file1 = Path(asm_directory, f"{program}.cmp")
    file2 = Path(asm_directory, f"{program}.hack")

    with file1.open() as f1, file2.open() as f2:
        for i, (line1, line2) in enumerate(zip_longest(f1, f2), 1):
            if line1 != line2:
                print(f"Difference at line {i}:\n{file1.name}: {line1.rstrip()}\n{file2.name}: {line2.rstrip()}")
                break
        else:
            print("Files are the same in content (but maybe metadata differs)")

    assert filecmp.cmp(
        shallow=False,
        f1=str(asm_directory.joinpath(f"{program}.cmp")),
        f2=str(asm_directory.joinpath(f"{program}.hack")),
    )
