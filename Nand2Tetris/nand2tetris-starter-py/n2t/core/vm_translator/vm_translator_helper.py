from typing import Iterable, List


def remove_comments_and_whitespaces(lines: Iterable[str]) -> List[str]:
    result = []
    for line in lines:
        line = line.split("//", 1)[0].strip()
        if line:
            result.append(line)
    return result


def get_push_segment_i_asm_code(memory_segment: str, i: int, file_name: str) -> List[str]:
    result = ["// push " + memory_segment + " " + str(i)]

    # D=RAM[segment_base_address + i]
    if memory_segment == "local" or memory_segment == "argument" or memory_segment == "this" or memory_segment == "that":
        segment_base_address_address: str
        if memory_segment == "local":
            segment_base_address_address = "LCL"
        elif memory_segment == "argument":
            segment_base_address_address = "ARG"
        elif memory_segment == "this":
            segment_base_address_address = "THIS"
        else: # that
            segment_base_address_address = "THAT"

        result.append("@" + str(segment_base_address_address))
        result.append("D=M")
        result.append("@" + str(i))
        result.append("D=D+A")
        result.append("A=D")
        result.append("D=M")

    elif memory_segment == "temp":
        result.append("@5")
        result.append("D=A")
        result.append("@" + str(i))
        result.append("D=D+A")
        result.append("A=D")
        result.append("D=M")

    elif memory_segment == "static":
        result.append("@" + file_name + "." + str(i))
        result.append("D=M")

    elif memory_segment == "pointer":
        if i == 0:
            result.append("@THIS")
            result.append("D=M")
        else:
            result.append("@THAT")
            result.append("D=M")

    else: # constant
        result.append("@" + str(i))
        result.append("D=A")

    # RAM[SP]=D
    result.append("@SP")
    result.append("A=M")
    result.append("M=D")

    # SP++
    result.append("@SP")
    result.append("M=M+1")

    return result


def translate_arithmetic_or_logical_instruction(instruction_type: str, line_number: int) -> List[str]:
    result: List[str] = ["// " + instruction_type]
    if instruction_type == "add":
        result.append("@SP")
        result.append("AM=M-1")
        result.append("D=M")
        result.append("A=A-1")
        result.append("M=M+D")
    if instruction_type == "sub":
        result.append("@SP")
        result.append("AM=M-1")
        result.append("D=M")
        result.append("A=A-1")
        result.append("M=M-D")
    if instruction_type == "and":
        result.append("@SP")
        result.append("AM=M-1")
        result.append("D=M")
        result.append("A=A-1")
        result.append("M=M&D")
    if instruction_type == "or":
        result.append("@SP")
        result.append("AM=M-1")
        result.append("D=M")
        result.append("A=A-1")
        result.append("M=M|D")
    if instruction_type == "neg":
        result.append("@SP")
        result.append("A=M-1")
        result.append("M=-M")
    if instruction_type == "not":
        result.append("@SP")
        result.append("A=M-1")
        result.append("M=!M")
    if instruction_type == "eq":
        result.append("@SP")
        result.append("AM=M-1")
        result.append("D=M")
        result.append("A=A-1")
        result.append("D=M-D")
        result.append("M=-1")
        result.append("@END" + str(line_number))
        result.append("D;JEQ")
        result.append("@SP")
        result.append("A=M-1")
        result.append("M=0")
        result.append("(END" + str(line_number) + ")")
    if instruction_type == "gt":
        result.append("@SP")
        result.append("AM=M-1")
        result.append("D=M")
        result.append("A=A-1")
        result.append("D=M-D")
        result.append("M=-1")
        result.append("@END" + str(line_number))
        result.append("D;JGT")
        result.append("@SP")
        result.append("A=M-1")
        result.append("M=0")
        result.append("(END" + str(line_number) + ")")
    if instruction_type == "lt":
        result.append("@SP")
        result.append("AM=M-1")
        result.append("D=M")
        result.append("A=A-1")
        result.append("D=M-D")
        result.append("M=-1")
        result.append("@END" + str(line_number))
        result.append("D;JLT")
        result.append("@SP")
        result.append("A=M-1")
        result.append("M=0")
        result.append("(END" + str(line_number) + ")")

    return result


def get_pop_segment_i_asm_code(memory_segment: str, i: int, file_name: str) -> List[str]:
    result = ["// pop " + memory_segment + " " + str(i)]

    # RAM[memory_segment + i]=D
    if memory_segment == "local" or memory_segment == "argument" or memory_segment == "this" or memory_segment == "that":
        segment_base_address_address: str
        if memory_segment == "local":
            segment_base_address_address = "LCL"
        elif memory_segment == "argument":
            segment_base_address_address = "ARG"
        elif memory_segment == "this":
            segment_base_address_address = "THIS"
        else:  # that
            segment_base_address_address = "THAT"

        result.append("@" + str(segment_base_address_address))
        result.append("D=M")
        result.append("@" + str(i))
        result.append("D=D+A")
        result.append("@R13")
        result.append("M=D")

    elif memory_segment == "pointer":
        if i == 0:
            result.append("@THIS")
        else:
            result.append("@THAT")
        result.append("D=A")
        result.append("@R13")
        result.append("M=D")

    elif memory_segment == "temp":
        result.append("@5")
        result.append("D=A")
        result.append("@" + str(i))
        result.append("D=D+A")
        result.append("@R13")
        result.append("M=D")

    elif memory_segment == "static":
        result.append("@SP")
        result.append("AM=M-1")
        result.append("D=M")
        result.append("@" + file_name + "." + str(i))
        result.append("M=D")

    # SP--
    result.append("@SP")
    result.append("AM=M-1")

    # D=RAM[SP-1]
    result.append("D=M")
    result.append("@R13")
    result.append("A=M")
    result.append("M=D")

    return result


def get_label_code(label_name: str) -> List[str]:
    return [
        "// label " + label_name,
        "(" + label_name + ")"
    ]


def get_if_goto_code(label_name: str) -> List[str]:
    return [
        "// if-goto " + label_name,
        "@SP",
        "AM=M-1",
        "D=M",
        "@" + label_name,
        "D;JNE"
    ]


def get_goto_code(label_name: str) -> List[str]:
    return [
        "// goto " + label_name,
        "@" + label_name,
        "0;JMP"
    ]


def get_function_declaration_code(function_name: str, local_variable_count: int, file_name: str) -> List[str]:
    result: List[str] = []
    # (functionName)
    result.extend(get_label_code(function_name))
    # init local variables to 0
    for i in range(0, local_variable_count):
        result.extend(get_push_segment_i_asm_code("constant", 0, file_name))

    return result


def get_return_code() -> List[str]:
    return [
        "// return",
        # endFrame = LCL
        "@LCL",
        "D=M",
        "@endFrame",
        "M=D",
        # retAddr = *(endFrame - 5)
        "@5",
        "D=A",
        "@endFrame",
        "A=M-D",
        "D=M",
        "@retAddr",
        "M=D",
        # *ARG = pop()
        "@SP",
        "AM=M-1",
        "D=M",
        "@ARG",
        "A=M",
        "M=D",
        # SP = ARG + 1
        "@ARG",
        "D=M+1",
        "@SP",
        "M=D",
        # THAT = *(endFrame - 1)
        "@endFrame",
        "AM=M-1",
        "D=M",
        "@THAT",
        "M=D",
        # THIS = *(endFrame - 2)
        "@endFrame",
        "AM=M-1",
        "D=M",
        "@THIS",
        "M=D",
        # ARG = *(endFrame - 3)
        "@endFrame",
        "AM=M-1",
        "D=M",
        "@ARG",
        "M=D",
        # LCL = *(endFrame - 4)
        "@endFrame",
        "AM=M-1",
        "D=M",
        "@LCL",
        "M=D",
        # goto redAddr
        "@retAddr",
        "A=M",
        "0;JMP"
    ]


def get_function_call_code(function_name: str, arg_count: int, index: int) -> List[str]:
    label_name = function_name + "$ret." + str(index)
    result: List[str] = [
        # push returnAddress
        "@" + label_name,
        "D=A",
        "@SP",
        "A=M",
        "M=D",
        "@SP",
        "M=M+1",
    ]

    # push LCL, ARG, THIS, THAT
    for memory_segment in ["LCL", "ARG", "THIS", "THAT"]:
        result.extend([
            "@" + memory_segment,
            "D=M",
            "@SP",
            "A=M",
            "M=D",
            "@SP",
            "M=M+1"
        ])

    result.extend([
        # ARG = SP-5-arg_count
        "@SP",
        "D=M",
        "@5",
        "D=D-A",
        "@" + str(arg_count),
        "D=D-A",
        "@ARG",
        "M=D",
        # LCL = SP
        "@SP",
        "D=M",
        "@LCL",
        "M=D",
    ])

    # goto function
    result.extend(get_goto_code(function_name))
    # (returnAddress)
    result.extend(get_label_code(label_name))

    return result


def translate_to_asm(instructions: Iterable[str], vm_file_name: str, is_dir: bool) -> List[str]:
    result: List[str] = []

    if is_dir:
        result.extend([
            "@256",
            "D=A",
            "@SP",
            "M=D",
        ])
        result.extend(get_function_declaration_code("OS", 0, vm_file_name))
        result.extend(get_function_call_code("Sys.init", 0, -1))

    for index, instruction in enumerate(instructions):
        instruction_parts = instruction.split(" ")
        part0 = instruction_parts[0]

        if len(instruction_parts) == 1 and part0 != "return": # eq, sub, add...
            result.extend(translate_arithmetic_or_logical_instruction(part0, index))
            continue

        elif part0 == "return":
            result.extend(get_return_code())
            continue

        part1 = instruction_parts[1]

        if part0 == "label":
            result.extend(get_label_code(part1))
            continue
        elif part0 == "if-goto":
            result.extend(get_if_goto_code(part1))
            continue
        elif part0 == "goto":
            result.extend(get_goto_code(part1))
            continue

        part2 = instruction_parts[2]

        if part0 == "push":
            result.extend(get_push_segment_i_asm_code(part1, int(part2), vm_file_name))

        elif part0 == "pop":
            result.extend(get_pop_segment_i_asm_code(part1, int(part2), vm_file_name))

        elif part0 == "function":
            result.extend(get_function_declaration_code(part1, int(part2), vm_file_name))

        elif part0 == "call":
            result.extend(get_function_call_code(part1, int(part2), index))

    return result
