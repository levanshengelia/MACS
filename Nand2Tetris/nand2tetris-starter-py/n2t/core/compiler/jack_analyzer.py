import os
from pathlib import Path

from n2t.core.compiler import file_helper
from n2t.core.compiler.compilation_engine import CompilationEngine
from n2t.core.compiler.jack_tokenizer import JackTokenizer
from n2t.core.compiler.vm_writer import VMWriter


def analyze(file_or_directory_path: str) -> None:
    directory = file_helper.get_directory(file_or_directory_path)

    for file_name in os.listdir(directory):
        if not file_name.endswith(".jack"):
            continue

        jack_tokenizer = JackTokenizer(os.path.join(directory, file_name))

        jack_file = Path(file_name)
        vm_file = jack_file.with_name(jack_file.stem + ".vm")
        full_path = os.path.join(directory, vm_file)

        with open(full_path, "w") as f:
            writer = VMWriter(f)
            compilation_engine = CompilationEngine(jack_tokenizer, writer)
            compilation_engine.compile_class()
            writer.close()


