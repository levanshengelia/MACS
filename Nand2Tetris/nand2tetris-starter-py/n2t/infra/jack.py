from __future__ import annotations

from dataclasses import dataclass

from n2t.core.compiler import jack_analyzer


@dataclass
class JackProgram:  # TODO: your work for Projects 10 and 11 starts here
    @classmethod
    def load_from(cls, file_or_directory_name: str) -> JackProgram:
        cls.file_or_directory_name = file_or_directory_name
        return cls()

    def compile(self) -> None:
        jack_analyzer.analyze(self.file_or_directory_name)
