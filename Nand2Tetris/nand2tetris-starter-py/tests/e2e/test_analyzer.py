import filecmp
import os
from pathlib import Path

import pytest

from n2t.infra.io import remove_files
from n2t.runner.cli import run_compiler

_TEST_PROGRAMS = ["ArrayTest"]


# @pytest.mark.skip
@pytest.mark.parametrize("test_directory", _TEST_PROGRAMS)
def test_should_analyze_on_files(test_directory: str, jacks_directory: Path) -> None:
    projects_directory_path = os.path.join(jacks_directory, test_directory)
    file_path = os.path.join(projects_directory_path, "Main.jack")

    run_compiler(file_path)

    assert filecmp.cmp(
        shallow=False,
        f1=str(Path(projects_directory_path).joinpath("MainT.xml.cmp")),
        f2=str(Path(projects_directory_path).joinpath("MainT.xml")),
    )

    assert filecmp.cmp(
        shallow=False,
        f1=str(Path(projects_directory_path).joinpath("Main.xml.cmp")),
        f2=str(Path(projects_directory_path).joinpath("Main.xml")),
    )

    remove_files(pattern=str(Path(projects_directory_path).joinpath("*.xml")))
    remove_files(pattern=str(Path(projects_directory_path).joinpath("*.vm")))


_TEST_PROGRAMS = ["ArrayTest", "ExpressionLessSquare", "Square"]


# @pytest.mark.skip
@pytest.mark.parametrize("test_directory", _TEST_PROGRAMS)
def test_should_analyze_on_directory(
    test_directory: str, jacks_directory: Path
) -> None:
    projects_directory_path = os.path.join(jacks_directory, test_directory)

    run_compiler(projects_directory_path)

    xml_files = list(Path(projects_directory_path).glob("*.xml"))
    jack_files = list(Path(projects_directory_path).glob("*.jack"))

    assert len(xml_files) == len(jack_files) * 2

    for xml_file in xml_files:
        assert filecmp.cmp(
            shallow=False,
            f1=str(xml_file) + ".cmp",
            f2=str(xml_file),
        )

    remove_files(pattern=str(Path(projects_directory_path).joinpath("*.xml")))
    remove_files(pattern=str(Path(projects_directory_path).joinpath("*.vm")))
