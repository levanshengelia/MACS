from enum import Enum


class MemorySegment(Enum):
    CONSTANT = 1
    ARGUMENT = 2
    LOCAL = 3
    STATIC = 4
    TEMP = 5
    POINTER = 6,
    THIS = 7,
    THAT = 8


class VMWriter:
    def __init__(self, output_stream):
        self.output_stream = output_stream

    def write_push(self, segment: MemorySegment, index: int):
        self._write(f"push {segment.name.lower()} {index}")

    def write_pop(self, segment: MemorySegment, index: int):
        self._write(f"pop {segment.name.lower()} {index}")

    def write_arithmetic(self, command: str):
        self._write(command.lower())

    def write_label(self, label: str):
        self._write(f"label {label}")

    def write_goto(self, label: str):
        self._write(f"goto {label}")

    def write_if(self, label: str):
        self._write(f"if-goto {label}")

    def write_call(self, name: str, n_args: int):
        self._write(f"call {name} {n_args}")

    def write_function(self, name: str, n_locals: int):
        self._write(f"function {name} {n_locals}")

    def write_return(self):
        self._write("return")

    def write_empty_line(self):
        self._write("")

    def close(self):
        self.output_stream.close()

    def _write(self, command: str):
        self.output_stream.write(command + "\n")
