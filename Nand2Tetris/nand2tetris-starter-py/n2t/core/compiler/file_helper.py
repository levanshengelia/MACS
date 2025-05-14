import os
from pathlib import Path
from typing import List
from xml.dom.minidom import parseString


def get_directory(file_or_directory_path):
    if os.path.isfile(file_or_directory_path):
        return os.path.dirname(file_or_directory_path)
    else:
        return file_or_directory_path


def create_xml_T_file(lines: List[str], file_name: str, directory: str) -> None:
    jack_file = Path(file_name)
    xml_file = jack_file.with_name(jack_file.stem + "T.xml")
    full_path = os.path.join(directory, xml_file)

    with open(full_path, "w", encoding="utf-8") as f:
        for line in lines:
            f.write(line + "\n")


def create_xml_file(lines: List[str], file_name: str, directory: str) -> None:
    jack_file = Path(file_name)
    xml_file = jack_file.with_name(jack_file.stem + ".xml")
    full_path = os.path.join(directory, xml_file)

    raw_xml = "\n".join(lines)
    dom = parseString(raw_xml)

    pretty_xml = dom.toprettyxml(indent="  ")

    formatted_lines = [
        line for line in lines
        if line.strip() and not line.startswith("<?xml") and line.strip() not in ("<remove>", "</remove>")
    ]

    with open(full_path, "w", encoding="utf-8") as f:
        f.write("\n".join(formatted_lines))
        f.write("\n") # that's nasty fix but paidziot
