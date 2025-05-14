import os
from pathlib import Path

import pytest

from tests.e2e.conftest import run_vm_translator_test

_TEST_PROGRAMS = [
    ("FunctionCalls", "SimpleFunction"),
    ("ProgramFlow", "BasicLoop"),
    ("ProgramFlow", "FibonacciSeries"),
]


def run_for_all_os(
    test_file: tuple[str, str], cpu_emulator: Path, projects_directory: Path
) -> None:
    project_part, directory_name = test_file
    projects_directory_path = os.path.join(
        projects_directory, "8", project_part, directory_name
    )

    tst_file = directory_name + ".tst"
    vm_file = directory_name + ".vm"
    run_vm_translator_test(cpu_emulator, projects_directory_path, tst_file, vm_file)


@pytest.mark.skip
@pytest.mark.parametrize("test_file", _TEST_PROGRAMS)
def test_run_emulator_bat(
    test_file: tuple[str, str], cpu_emulator_bat: Path, projects_directory: Path
) -> None:
    run_for_all_os(test_file, cpu_emulator_bat, projects_directory)


@pytest.mark.skip
@pytest.mark.parametrize("test_file", _TEST_PROGRAMS)
def test_run_emulator_sh(
    test_file: tuple[str, str], cpu_emulator_sh: Path, projects_directory: Path
) -> None:
    run_for_all_os(test_file, cpu_emulator_sh, projects_directory)
