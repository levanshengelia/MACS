from enum import Enum
from n2t.core.compiler.jack_tokenizer import JackTokenizer, TokenType
from n2t.core.compiler.symbol_table import SymbolTable, Kind
from n2t.core.compiler.vm_writer import VMWriter, MemorySegment


class VarDecReason(Enum):
    DEFINITION = 1,
    USE = 2,

def get_kind(kind: str):
    if kind == 'static':
        return Kind.STATIC
    elif kind == 'field':
        return Kind.FIELD
    elif kind == 'var':
        return Kind.VAR
    else:
        raise ValueError('Unknown kind {}'.format(kind))


def _get_memory_segment_from_kind(kind: Kind) -> MemorySegment:
    if kind == Kind.VAR:
        return MemorySegment.LOCAL
    if kind == Kind.FIELD:
        return MemorySegment.THIS
    if kind == Kind.STATIC:
        return MemorySegment.STATIC
    if kind == Kind.ARG:
        return MemorySegment.ARGUMENT
    raise RuntimeError(f"Unknown kind {kind}")


class CompilationEngine:
    def __init__(self, jack_tokenizer: JackTokenizer, vm_writer: VMWriter) -> None:
        self.class_name = None
        self.tokenizer: JackTokenizer = jack_tokenizer
        self.vm_writer = vm_writer
        self.symbol_table: SymbolTable = SymbolTable()
        self.running_num = 0

        if self.tokenizer.has_more_tokens():
            self.tokenizer.advance()

        if self.tokenizer.get_token_type() == TokenType.KEYWORD:
            keyword = self.tokenizer.get_keywords()

            if not keyword == "class":
                raise ValueError("file must start with 'class'")

    def _eat(self, expected: str = None) -> None:
        if expected is not None and self.tokenizer.current_token != expected:
            raise ValueError("expected {}, got {}".format(expected, self.tokenizer.current_token))
        self.tokenizer.advance()

    def compile_class(self) -> None:
        self._eat("class")
        self.class_name = self.tokenizer.current_token
        self._eat()
        self._eat("{")

        field_count = 0
        while self.tokenizer.current_token in ("field", "static"):
            if self.tokenizer.current_token == "field":
                field_count += self.compile_class_var_dec()
            elif self.tokenizer.current_token == "static":
                self.compile_class_var_dec()

        while self.tokenizer.current_token in ("constructor", "function", "method"):
            if self.tokenizer.current_token == "constructor":
                self.compile_method_dec(field_count, False, True)
            if self.tokenizer.current_token == "method":
                self.compile_method_dec(0, True, False)
            elif self.tokenizer.current_token == "function":
                self.compile_method_dec(0, False, False)
            self.vm_writer.write_empty_line()

        self._eat("}")

    def compile_class_var_dec(self) -> int:
        var_count = 1
        kind = get_kind(self.tokenizer.current_token)
        self._eat()
        type_ = self.tokenizer.current_token
        self._eat()
        self.symbol_table.define(self.tokenizer.current_token, type_, kind, False)
        self._eat()
        while self.tokenizer.current_token == ",":
            var_count += 1
            self._eat(",")
            self.symbol_table.define(self.tokenizer.current_token, type_, kind, False)
            self._eat()
        self._eat(";")
        return var_count

    def compile_method_dec(self, field_count: int, is_method: bool, is_ctor: bool) -> None:
        self._eat()
        self._eat()
        method_name = self.tokenizer.current_token
        self._eat()
        self._eat("(")
        self.symbol_table.start_subroutine()
        self.compile_parameter_list(is_method)
        self._eat(")")
        self.compile_method_body(self.class_name + "." + method_name, field_count, is_method, is_ctor)

    def compile_parameter_list(self, is_method: bool) -> None:
        if self.tokenizer.current_token != ")":
            type_ = self.tokenizer.current_token
            self._eat()
            self.symbol_table.define(self.tokenizer.current_token, type_, Kind.ARG, is_method)
            self._eat()
            while self.tokenizer.current_token == ",":
                self._eat(",")
                type_ = self.tokenizer.current_token
                self._eat()
                self.symbol_table.define(self.tokenizer.current_token, type_, Kind.ARG, is_method)
                self._eat()

    def compile_method_body(self, method_name, field_count: int, is_method: bool, is_ctor: bool) -> None:
        self._eat("{")
        var_count = 0
        while self.tokenizer.current_token == "var":
            var_count += self.compile_var_dec()
        self.vm_writer.write_function(method_name, var_count)
        if field_count > 0:
            self.vm_writer.write_push(MemorySegment.CONSTANT, field_count)
            self.vm_writer.write_call("Memory.alloc", 1)
            self.vm_writer.write_pop(MemorySegment.POINTER, 0)
            self.vm_writer.write_empty_line()
        if is_method:
            self.vm_writer.write_push(MemorySegment.ARGUMENT, 0)
            self.vm_writer.write_pop(MemorySegment.POINTER, 0)
        self.compile_statements(is_method, is_ctor)
        self._eat("}")

    def compile_var_dec(self) -> int:
        var_count = 1
        self._eat("var")
        type_ = self.tokenizer.current_token
        self._eat()
        self.symbol_table.define(self.tokenizer.current_token, type_, Kind.VAR, False)
        self._eat()
        while self.tokenizer.current_token == ",":
            self._eat(",")
            self.symbol_table.define(self.tokenizer.current_token, type_, Kind.VAR, False)
            self._eat()
            var_count += 1
        self._eat(";")
        return var_count

    def compile_statements(self, is_method: bool, is_ctor: bool) -> None:
        while self.tokenizer.current_token in ("let", "if", "while", "do", "return"):
            if self.tokenizer.current_token == "let":
                self.compile_let()
            elif self.tokenizer.current_token == "if":
                self.compile_if(is_method, is_ctor)
            elif self.tokenizer.current_token == "while":
                self.compile_while(is_method, is_ctor)
            elif self.tokenizer.current_token == "do":
                self.compile_do(is_method, is_ctor)
                self.vm_writer.write_pop(MemorySegment.TEMP, 0)
            elif self.tokenizer.current_token == "return":
                self.compile_return()

    def compile_let(self) -> None:
        self._eat("let")
        variable = self.tokenizer.current_token
        self._eat()
        is_array = False
        if self.tokenizer.current_token == "[":
            self._eat("[")
            kind = self.symbol_table.kind_of(variable)
            index = self.symbol_table.index_of(variable)
            segment = _get_memory_segment_from_kind(kind)
            self.vm_writer.write_push(segment, index)
            self.compile_expression()
            self.vm_writer.write_arithmetic("add")
            self.vm_writer.write_pop(MemorySegment.TEMP, 0)
            self._eat("]")
            is_array = True
        self._eat("=")
        self.compile_expression()

        if is_array:
            self.vm_writer.write_push(MemorySegment.TEMP, 0)
            self.vm_writer.write_pop(MemorySegment.POINTER, 1)
            self.vm_writer.write_pop(MemorySegment.THAT, 0)
        else:
            kind = self.symbol_table.kind_of(variable)
            index = self.symbol_table.index_of(variable)

            if kind == Kind.VAR:
                self.vm_writer.write_pop(MemorySegment.LOCAL, index)
            elif kind == Kind.ARG:
                self.vm_writer.write_pop(MemorySegment.ARGUMENT, index)
            elif kind == Kind.STATIC:
                self.vm_writer.write_pop(MemorySegment.STATIC, index)
            elif kind == Kind.FIELD:
                self.vm_writer.write_pop(MemorySegment.THIS, index)

        self._eat(";")

    def compile_if(self, is_method: bool, is_ctor: bool) -> None:
        num = self.running_num
        self.running_num += 1
        if_body_label = "if_body_" + str(num)
        if_body_end = "if_end_" + str(num)
        end = "end_" + str(num)

        self._eat("if")
        self._eat("(")
        self.compile_expression()
        self._eat(")")
        self._eat("{")
        self.vm_writer.write_if(if_body_label)
        self.vm_writer.write_goto(if_body_end)
        self.vm_writer.write_label(if_body_label)
        self.compile_statements(is_method, is_ctor)
        self.vm_writer.write_goto(end)
        self._eat("}")
        self.vm_writer.write_label(if_body_end)
        if self.tokenizer.current_token == "else":
            self._eat("else")
            self._eat("{")
            self.compile_statements(is_method, is_ctor)
            self._eat("}")
        self.vm_writer.write_label(end)

    def compile_while(self, is_method: bool, is_ctor: bool) -> None:
        num = self.running_num
        start_label = "while_start_" + str(num)
        end_label = "while_end_" + str(num)
        body_label = "while_body_" + str(num)

        self.running_num += 1
        self.vm_writer.write_label(start_label)
        self._eat("while")
        self._eat("(")
        self.compile_expression()
        self.vm_writer.write_if(body_label)
        self.vm_writer.write_goto(end_label)
        self._eat(")")
        self._eat("{")
        self.vm_writer.write_label(body_label)
        self.compile_statements(is_method, is_ctor)
        self._eat("}")
        self.vm_writer.write_goto(start_label)
        self.vm_writer.write_label(end_label)

    def compile_do(self, is_method: bool, is_ctor) -> None:
        arg_num = 0
        flag = False
        self._eat("do")
        called_method_name = self.tokenizer.current_token
        self._eat()
        if self.tokenizer.current_token == ".":
            self._eat(".")
            called_method_name += "." + self.tokenizer.current_token
            self._eat()
        else:
            flag = True
            called_method_name = self.class_name + "." + called_method_name
        self._eat("(")
        part0 = called_method_name.split('.')[0]
        if self.symbol_table.kind_of(part0) in (Kind.VAR, Kind.ARG, Kind.FIELD, Kind.STATIC):
            index = self.symbol_table.index_of(part0)
            kind = self.symbol_table.kind_of(part0)
            memory_segment = _get_memory_segment_from_kind(kind)
            self.vm_writer.write_push(memory_segment, index)
            called_method_name = self.symbol_table.type_of(part0) + "." + called_method_name.split(".")[1]
            arg_num += 1
        if flag and (is_method or is_ctor):
            self.vm_writer.write_push(MemorySegment.POINTER, 0)
            arg_num += 1
        arg_num += self.compile_expression_list()
        self._eat(")")
        self._eat(";")
        self.vm_writer.write_call(called_method_name, arg_num)

    def compile_return(self) -> None:
        self._eat("return")
        is_void_function = True
        if self.tokenizer.current_token != ";":
            self.compile_expression()
            is_void_function = False
        self._eat(";")
        if is_void_function:
            self.vm_writer.write_push(MemorySegment.CONSTANT, 0)
        self.vm_writer.write_return()

    def compile_expression(self) -> None:
        self.compile_term()
        while self.tokenizer.current_token in ('+', '-', '*', '/', '&', '|', '<', '>', '='):
            command = self.tokenizer.current_token
            self._eat(command)
            self.compile_term()

            if command == '+':
                self.vm_writer.write_arithmetic("add")
            if command == '-':
                self.vm_writer.write_arithmetic("sub")
            elif command == '*':
                self.vm_writer.write_call("Math.multiply", 2)
            elif command == '/':
                self.vm_writer.write_call("Math.divide", 2)
            elif command == '&':
                self.vm_writer.write_arithmetic("and")
            elif command == '|':
                self.vm_writer.write_arithmetic("or")
            elif command == '>':
                self.vm_writer.write_arithmetic("gt")
            elif command == '<':
                self.vm_writer.write_arithmetic("lt")
            elif command == '=':
                self.vm_writer.write_arithmetic("eq")

    def compile_term(self) -> None:
        if self.tokenizer.get_token_type() in (TokenType.INT_CONST, TokenType.STRING_CONST, TokenType.KEYWORD):
            if self.tokenizer.current_token == "true":
                self.vm_writer.write_push(MemorySegment.CONSTANT, 0)
                self.vm_writer.write_arithmetic("not")
            elif self.tokenizer.current_token == "false" or self.tokenizer.current_token == "null":
                self.vm_writer.write_push(MemorySegment.CONSTANT, 0)
            elif self.tokenizer.current_token == "this":
                self.vm_writer.write_push(MemorySegment.POINTER, 0)
            elif self.tokenizer.get_token_type() == TokenType.INT_CONST:
                self.vm_writer.write_push(MemorySegment.CONSTANT, self.tokenizer.get_int_value())
            elif self.tokenizer.get_token_type() == TokenType.STRING_CONST:
                self._handle_string(self.tokenizer.current_token)
            self._eat()
        elif self.tokenizer.get_token_type() == TokenType.IDENTIFIER:
            identifier = self.tokenizer.current_token
            self._eat()
            if self.tokenizer.current_token == "[":
                self._eat("[")
                kind = self.symbol_table.kind_of(identifier)
                index = self.symbol_table.index_of(identifier)
                segment = _get_memory_segment_from_kind(kind)
                self.vm_writer.write_push(segment, index)
                self.compile_expression()
                self.vm_writer.write_arithmetic("add")
                self.vm_writer.write_pop(MemorySegment.POINTER, 1)
                self.vm_writer.write_push(MemorySegment.THAT, 0)
                self._eat("]")
            elif self.tokenizer.current_token in ("(", "."):
                function_name = identifier
                args_count = 0
                if self.tokenizer.current_token == ".":
                    self._eat(".")
                    function_name += "." + self.tokenizer.current_token
                    self._eat()
                else:
                    function_name += self.class_name + "." + function_name
                    self.vm_writer.write_push(MemorySegment.POINTER, 0)
                    args_count += 1
                self._eat("(")
                if self.symbol_table.kind_of(identifier) in (Kind.VAR, Kind.ARG, Kind.FIELD, Kind.STATIC):
                    index = self.symbol_table.index_of(identifier)
                    kind = self.symbol_table.kind_of(identifier)
                    memory_segment = _get_memory_segment_from_kind(kind)
                    self.vm_writer.write_push(memory_segment, index)
                    function_name = self.symbol_table.type_of(identifier) + "." + function_name.split(".")[1]
                    args_count += 1
                args_count += self.compile_expression_list()
                self._eat(")")
                self.vm_writer.write_call(function_name, args_count)
            elif self.symbol_table.kind_of(identifier) is not None:
                index = self.symbol_table.index_of(identifier)
                kind = self.symbol_table.kind_of(identifier)
                memory_segment = _get_memory_segment_from_kind(kind)
                self.vm_writer.write_push(memory_segment, index)
            else:
                raise ValueError("invalid token")
        elif self.tokenizer.current_token == "(":
            self._eat("(")
            self.compile_expression()
            self._eat(")")
        elif self.tokenizer.current_token in ("-", "~"):
            operator = self.tokenizer.current_token
            self._eat()
            self.compile_term()
            if operator == "-":
                self.vm_writer.write_arithmetic("neg")
            elif operator == "~":
                self.vm_writer.write_arithmetic("not")

    def compile_expression_list(self) -> int:
        arg_num = 0
        if self.tokenizer.current_token != ")":
            arg_num += 1
            self.compile_expression()
            while self.tokenizer.current_token == ",":
                arg_num += 1
                self._eat(",")
                self.compile_expression()

        return arg_num

    def _handle_string(self, string_const: str):
        string_const = string_const.strip('"')
        self.vm_writer.write_push(MemorySegment.CONSTANT, len(string_const))
        self.vm_writer.write_call("String.new", 1)
        for char in string_const:
            self.vm_writer.write_push(MemorySegment.CONSTANT, ord(char))
            self.vm_writer.write_call("String.appendChar", 2)
