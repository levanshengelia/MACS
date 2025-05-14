from __future__ import annotations

from dataclasses import dataclass
from typing import List
from pathlib import Path

from n2t.core.vm_translator import vm_translator_helper, file_helper_methods


@dataclass
class VmProgram:  # TODO: your work for Projects 7 and 8 starts here
    def __init__(self, file_or_directory_name: str):
        self.file_or_directory_name = file_or_directory_name

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> VmProgram:
        return cls(file_or_directory_name)

    def translate(self) -> None:
        vm_files = file_helper_methods.get_vm_files(self.file_or_directory_name)
        result: List[str] = []
        for vm_file in vm_files:
            file_content = file_helper_methods.read_file_content(vm_file)
            instructions = vm_translator_helper.remove_comments_and_whitespaces(file_content)
            asm_instructions = vm_translator_helper.translate_to_asm(instructions,  Path(vm_file).stem, len(vm_files) > 1)
            result.extend(asm_instructions)
        file_helper_methods.generate_asm_file(result, self.file_or_directory_name)
