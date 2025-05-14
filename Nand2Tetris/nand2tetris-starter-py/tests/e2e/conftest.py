import os
import subprocess
from pathlib import Path
from typing import Iterable

import pytest

from n2t.infra.io import remove_files
from n2t.runner.cli import run_vm_translator


@pytest.fixture(scope="module")
def hack_directory(pytestconfig: pytest.Config) -> Iterable[Path]:
    name = pytestconfig.rootpath.joinpath("tests", "e2e", "hack")

    yield name

    remove_files(pattern=str(name.joinpath("*.asm")))


@pytest.fixture(scope="module")
def asm_directory(pytestconfig: pytest.Config) -> Iterable[Path]:
    name = pytestconfig.rootpath.joinpath("tests", "e2e", "asm")

    yield name

    remove_files(pattern=str(name.joinpath("*.hack")))


@pytest.fixture(scope="module")
def jacks_directory(pytestconfig: pytest.Config) -> Iterable[Path]:
    name = pytestconfig.rootpath.joinpath("tests", "e2e", "jacks_for_analyzer")

    yield name


@pytest.fixture(scope="module")
def cpu_emulator_bat(pytestconfig: pytest.Config) -> Iterable[Path]:
    name = pytestconfig.rootpath.joinpath(
        "tests", "e2e", "nand2tetris", "tools", "CPUEmulator.bat"
    )

    yield name


@pytest.fixture(scope="module")
def cpu_emulator_sh(pytestconfig: pytest.Config) -> Iterable[Path]:
    name = pytestconfig.rootpath.joinpath(
        "tests", "e2e", "nand2tetris", "tools", "CPUEmulator.sh"
    )

    yield name


@pytest.fixture(scope="module")
def projects_directory(pytestconfig: pytest.Config) -> Iterable[Path]:
    name = pytestconfig.rootpath.joinpath("tests", "e2e", "nand2tetris", "projects")

    yield name


def run_vm_translator_test(
    cpu_emulator_bat: Path,
    projects_directory_path: str,
    test_file_name: str,
    vm_file: str = "",
) -> None:
    test_file_path = os.path.join(projects_directory_path, test_file_name)

    run_vm_translator(projects_directory_path + "/" + vm_file)

    command = f'"{cpu_emulator_bat}" "{test_file_path}"'
    process = subprocess.Popen(
        command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE
    )
    output = process.communicate()[0].decode("utf-8")
    assert "end of script - comparison ended successfully" in output.lower()
    remove_files(pattern=str(Path(projects_directory_path).joinpath("*.asm")))
    remove_files(pattern=str(Path(projects_directory_path).joinpath("*.out")))
