import re
from typing import Iterable


def get_predefined_symbols() -> dict[str, str]:
    return {
        "R0": "0",
        "R1": "1",
        "R2": "2",
        "R3": "3",
        "R4": "4",
        "R5": "5",
        "R6": "6",
        "R7": "7",
        "R8": "8",
        "R9": "9",
        "R10": "10",
        "R11": "11",
        "R12": "12",
        "R13": "13",
        "R14": "14",
        "R15": "15",
        "SCREEN": "16384",
        "KBD": "24576",
        "SP": "0",
        "LCL": "1",
        "ARG": "2",
        "THIS": "3",
        "THAT": "4"
    }


def get_labels(lines: Iterable[str]) -> dict[str, str]:
    result: dict[str, str] = {}

    counter = 0
    for line in lines:
        line = line.strip()

        if not line:
            continue

        if line.startswith("//"):
            continue

        match = re.match(r'\(\s*(\w+)\s*\)', line.strip())
        if match:
            label = match.group(1)
            result[label] = str(counter)
        else:
            counter += 1
    return result


def is_A_instruction(instruction: str) -> bool:
    return instruction.startswith('@')


def convert_A_instruction_to_binary(instruction: str, symbols_and_labels: dict[str, str], next_variable_address: int) -> (str, int):
    address_or_variable = instruction[1:]
    address_in_decimal:str

    is_variable = False
    for c in address_or_variable:
        if not c.isdigit():
            is_variable = True
            break

    if is_variable:
        if symbols_and_labels.__contains__(address_or_variable):
            address_in_decimal = symbols_and_labels[address_or_variable]
        else:
            address_in_decimal = str(next_variable_address)
            next_variable_address += 1
            symbols_and_labels[address_or_variable] = address_in_decimal
    else:
        address_in_decimal = address_or_variable

    address = int(address_in_decimal)
    address_in_binary = bin(address)[2:].zfill(15)
    return '0' + address_in_binary, next_variable_address


def get_dest(dest_name:str) -> str:
    match dest_name:
        case "" | "null":
            return "000"
        case "M":
            return "001"
        case "D":
            return "010"
        case "MD":
            return "011"
        case "A":
            return "100"
        case "AM":
            return "101"
        case "AD":
            return "110"
        case "AMD":
            return "111"
        case _:
            raise ValueError(f"Invalid dest mnemonic: {dest_name}")


def get_comp(comp_name:str) -> str:
    match comp_name:
        case "0":
            return "0101010"
        case "1":
            return "0111111"
        case "-1":
            return "0111010"
        case "D":
            return "0001100"
        case "A":
            return "0110000"
        case "!D":
            return "0001101"
        case "!A":
            return "0110001"
        case "-D":
            return "0001111"
        case "-A":
            return "0110011"
        case "D+1":
            return "0011111"
        case "A+1":
            return "0110111"
        case "D-1":
            return "0001110"
        case "A-1":
            return "0110010"
        case "D+A":
            return "0000010"
        case "D-A":
            return "0010011"
        case "A-D":
            return "0000111"
        case "D&A":
            return "0000000"
        case "D|A":
            return "0010101"
        case "M":
            return "1110000"
        case "!M":
            return "1110001"
        case "-M":
            return "1110011"
        case "M+1":
            return "1110111"
        case "M-1":
            return "1110010"
        case "D+M":
            return "1000010"
        case "D-M":
            return "1010011"
        case "M-D":
            return "1000111"
        case "D&M":
            return "1000000"
        case "D|M":
            return "1010101"
        case _:
            raise ValueError(f"Invalid comp mnemonic: {comp_name}")


def get_jump(jump_name:str) -> str:
    match jump_name:
        case "" | "null":
            return "000"
        case "JGT":
            return "001"
        case "JEQ":
            return "010"
        case "JGE":
            return "011"
        case "JLT":
            return "100"
        case "JNE":
            return "101"
        case "JLE":
            return "110"
        case "JMP":
            return "111"
        case _:
            raise ValueError(f"Invalid jump mnemonic: {jump_name}")


def convert_C_instruction_to_binary(instruction: str, symbols_and_labels: dict[str, str]) -> str:
    dest:str
    comp:str
    jump:str

    instr_split_by_equality = instruction.split('=', 1)
    if len(instr_split_by_equality) < 2:
        dest = '000'
    else:
        dest = get_dest(instr_split_by_equality[0])

    instr_split_by_semi_colon = instruction.split(';', 1)
    if len(instr_split_by_semi_colon) < 2:
        comp = get_comp(instr_split_by_equality[1])
        jump = '000'
    elif len(instr_split_by_equality) < 2:
        comp = get_comp(instr_split_by_semi_colon[0])
        jump = get_jump(instr_split_by_semi_colon[1])
    else:
        comp = get_comp(re.split(r'[;=]', instruction)[1])
        jump = get_jump(instr_split_by_semi_colon[1])

    return '111' + comp + dest + jump


def convert_assembly_to_binary(lines: Iterable[str], symbols_and_labels: dict[str, str]) -> Iterable[str]:
    result: list[str] = []
    next_variable_address = 16

    for line in lines:
        line_without_whitespaces = line.strip()

        if not line_without_whitespaces: # empty line
            continue

        if line_without_whitespaces.startswith("//"): # comment
            continue

        if line_without_whitespaces.startswith("("): # label
            continue

        instruction = line_without_whitespaces.split("//", 1)[0].strip() # ignore the comment

        if is_A_instruction(instruction): # A instruction
            binary, new_variable_address = convert_A_instruction_to_binary(instruction, symbols_and_labels, next_variable_address)
            next_variable_address = new_variable_address
            result.append(binary)
        else: # C instruction
            result.append(convert_C_instruction_to_binary(instruction, symbols_and_labels))

    return result


def get_file_content(assembly):
    with open(assembly.path, 'r') as f:
        return f.readlines()
