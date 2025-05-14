from typer import Typer, echo

from n2t.infra import AsmProgram, HackProgram, JackProgram, VmProgram

cli = Typer(
    name="Nand 2 Tetris Software",
    no_args_is_help=True,
    add_completion=False,
)


@cli.command("disassemble", no_args_is_help=True)
def run_disassembler(hack_file: str) -> None:
    echo(f"Disassembling {hack_file}")
    HackProgram.load_from(hack_file).disassemble()
    echo("Done!")


@cli.command("assemble", no_args_is_help=True)
def run_assembler(assembly_file: str) -> None:
    echo(f"Assembling {assembly_file}")
    AsmProgram.load_from(assembly_file).assemble()
    echo("Done!")


@cli.command("translate_vm", no_args_is_help=True)
def run_vm_translator(vm_file_or_directory: str) -> None:
    echo(f"Translating {vm_file_or_directory}")
    VmProgram.load_from(vm_file_or_directory).translate()
    echo("Done!")


@cli.command("compile", no_args_is_help=True)
def run_compiler(jack_file_or_directory: str) -> None:
    echo(f"Compiling {jack_file_or_directory}")
    JackProgram.load_from(jack_file_or_directory).compile()
    echo("Done!")
