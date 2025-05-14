# Nand2Tetris Python Starter

The repository contains (Python) starter project for Nand2Tetris spin-off course read in
[Free University of Tbilisi](https://www.freeuni.edu.ge/en), for the School of Math and Computer Science.

The original [Nand2Tetris](https://www.nand2tetris.org/) course was created by
[Noam Nisan](https://www.cs.huji.ac.il/~noam/) and [Shimon Schocken](https://www.shimonschocken.com/).
In a spirit to teach how to build a general-purpose computer system and a modern software hierarchy from the ground up.

## Coding Conventions

Keep python conventions in mind:

- variable_name
- function_name
- ClassName
- CONSTANT_NAME
- file_name.py

## Linters/Formatters

Use tools described below to avoid endless arguments related to coding style and formatting.
They help you to catch errors, and make code more readable for your peers.

- [ruff](https://docs.astral.sh/ruff/) to cache common style and formatting issues
- [mypy](https://github.com/python/mypy) to check your static types

Configuration for all the tools mentioned above is provided with the project.
You can use `make` to run each of these tools or see how to run them manually
inside the `Makefile`.

## Requirements

Use following command to install needed requirements

```bash
python -m pip install --upgrade pip
python -m pip install --upgrade poetry
poetry install --no-root
```

You can extend pyproject.toml with more packages if you need to.

## Make

you can use make to install/test/format/lint you project.

Note, to [install](https://stackoverflow.com/a/32127632) on Windows.

## Usage

Use following command to see usage instructions `python -m n2t --help`

## Licence

This project is licensed under the terms of the `MIT license`.
