import os.path
from typing import Iterable, List
from pathlib import Path


def read_file_content(filename: str) -> Iterable[str]:
    with open(filename, 'r') as f:
        return f.readlines()


def generate_asm_file(content: Iterable[str], file_or_directory_path: str) -> None:
    if os.path.isdir(file_or_directory_path):
        file_name = file_or_directory_path + '\\' + os.path.basename(file_or_directory_path) + '.asm'
    else:
        file_name = os.path.dirname(file_or_directory_path) + '\\' + Path(file_or_directory_path).stem + '.asm'

    with open(file_name, 'w') as f:
        for line in content:
            f.write(line + '\n')
    print("Generated asm file: " + file_name)


def get_vm_files(file_or_directory_name: str) -> List[str]:
    if os.path.isfile(file_or_directory_name) and file_or_directory_name.endswith(".vm"):
        return [file_or_directory_name]

    if os.path.isdir(file_or_directory_name):
        return [
            os.path.join(file_or_directory_name, f)
            for f in os.listdir(file_or_directory_name)
            if f.endswith(".vm") and os.path.isfile(os.path.join(file_or_directory_name, f))
        ]

    return []
