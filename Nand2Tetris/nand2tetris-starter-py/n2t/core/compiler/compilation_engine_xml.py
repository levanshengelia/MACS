from typing import List
from n2t.core.compiler.jack_tokenizer import JackTokenizer, TokenType
from n2t.core.compiler.symbol_table import SymbolTable


class CompilationEngineXml:
    def __init__(self, jack_tokenizer: JackTokenizer) -> None:
        self.tokenizer: JackTokenizer = jack_tokenizer
        self.result: List[str] = []
        self.resultT: List[str] = []
        self.symbol_table: SymbolTable = SymbolTable()

        if self.tokenizer.has_more_tokens():
            self.tokenizer.advance()

        if self.tokenizer.get_token_type() == TokenType.KEYWORD:
            keyword = self.tokenizer.get_keywords()

            if not keyword == "class":
                raise ValueError("file must start with 'class'")

            self.resultT.append('<tokens>')
            self.compile_class()
            self.resultT.append('</tokens>')

    def _write(self, value: str) -> None:
        self.result.append(value)
        self.resultT.append(value)

    def _write_token(self) -> None:
        token_type = self.tokenizer.get_token_type()

        if token_type == TokenType.KEYWORD:
            value = self.tokenizer.get_keywords()
            self._write(f"<keyword> {value} </keyword>")

        elif token_type == TokenType.SYMBOL:
            value = self.tokenizer.get_symbol()
            if value == "<":
                value = "&lt;"
            elif value == ">":
                value = "&gt;"
            elif value == "&":
                value = "&amp;"
            self._write(f"<symbol> {value} </symbol>")

        elif token_type == TokenType.IDENTIFIER:
            value = self.tokenizer.get_identifier()
            self._write(f"<identifier> {value} </identifier>")

        elif token_type == TokenType.INT_CONST:
            value = self.tokenizer.get_int_value()
            self._write(f"<integerConstant> {value} </integerConstant>")

        elif token_type == TokenType.STRING_CONST:
            value = self.tokenizer.get_string_value()
            self._write(f"<stringConstant> {value} </stringConstant>")

    def _eat(self, expected: str = None) -> None:
        if expected and self.tokenizer.current_token != expected:
            raise ValueError(f"Expected '{expected}' but got '{self.tokenizer.current_token}'")
        self._write_token()
        self.tokenizer.advance()

    def compile_class(self) -> None:
        self.result.append("<class>")
        self._eat("class")
        self._eat()
        self._eat("{")

        while self.tokenizer.current_token in ("static", "field"):
            self.compile_class_var_dec()

        while self.tokenizer.current_token in ("constructor", "function", "method"):
            self.compile_method_dec()

        self._eat("}")
        self.result.append("</class>")

    def compile_class_var_dec(self) -> None:
        self.result.append("<classVarDec>")
        self._eat()
        self._eat()
        self._eat()
        while self.tokenizer.current_token == ",":
            self._eat(",")
            self._eat()
        self._eat(";")
        self.result.append("</classVarDec>")

    def compile_method_dec(self) -> None:
        self.result.append("<subroutineDec>")
        self._eat()
        self._eat()
        self._eat()
        self._eat("(")
        self.compile_parameter_list()
        self._eat(")")
        self.compile_method_body()
        self.result.append("</subroutineDec>")

    def compile_parameter_list(self) -> None:
        self.result.append("<parameterList>")
        if self.tokenizer.current_token != ")":
            self._eat()
            self._eat()
            while self.tokenizer.current_token == ",":
                self._eat(",")
                self._eat()
                self._eat()

        self.result.append("<remove>")
        self.result.append("</remove>")

        self.result.append("</parameterList>")

    def compile_method_body(self) -> None:
        self.result.append("<subroutineBody>")
        self._eat("{")
        while self.tokenizer.current_token == "var":
            self.compile_var_dec()
        self.compile_statements()
        self._eat("}")
        self.result.append("</subroutineBody>")

    def compile_var_dec(self) -> None:
        self.result.append("<varDec>")
        self._eat("var")
        self._eat()
        self._eat()
        while self.tokenizer.current_token == ",":
            self._eat(",")
            self._eat()
        self._eat(";")
        self.result.append("</varDec>")

    def compile_statements(self) -> None:
        self.result.append("<statements>")
        while self.tokenizer.current_token in ("let", "if", "while", "do", "return"):
            if self.tokenizer.current_token == "let":
                self.compile_let()
            elif self.tokenizer.current_token == "if":
                self.compile_if()
            elif self.tokenizer.current_token == "while":
                self.compile_while()
            elif self.tokenizer.current_token == "do":
                self.compile_do()
            elif self.tokenizer.current_token == "return":
                self.compile_return()

        self.result.append("<remove>")
        self.result.append("</remove>")

        self.result.append("</statements>")

    def compile_let(self) -> None:
        self.result.append("<letStatement>")
        self._eat("let")
        self._eat()
        if self.tokenizer.current_token == "[":
            self._eat("[")
            self.compile_expression()
            self._eat("]")
        self._eat("=")
        self.compile_expression()
        self._eat(";")
        self.result.append("</letStatement>")

    def compile_if(self) -> None:
        self.result.append("<ifStatement>")
        self._eat("if")
        self._eat("(")
        self.compile_expression()
        self._eat(")")
        self._eat("{")
        self.compile_statements()
        self._eat("}")
        if self.tokenizer.current_token == "else":
            self._eat("else")
            self._eat("{")
            self.compile_statements()
            self._eat("}")
        self.result.append("</ifStatement>")

    def compile_while(self) -> None:
        self.result.append("<whileStatement>")
        self._eat("while")
        self._eat("(")
        self.compile_expression()
        self._eat(")")
        self._eat("{")
        self.compile_statements()
        self._eat("}")
        self.result.append("</whileStatement>")

    def compile_do(self) -> None:
        self.result.append("<doStatement>")
        self._eat("do")
        self._eat()
        if self.tokenizer.current_token == ".":
            self._eat(".")
            self._eat()
        self._eat("(")
        self.compile_expression_list()
        self._eat(")")
        self._eat(";")
        self.result.append("</doStatement>")

    def compile_return(self) -> None:
        self.result.append("<returnStatement>")
        self._eat("return")
        if self.tokenizer.current_token != ";":
            self.compile_expression()
        self._eat(";")
        self.result.append("</returnStatement>")

    def compile_expression(self) -> None:
        self.result.append("<expression>")
        self.compile_term()
        while self.tokenizer.current_token in ('+', '-', '*', '/', '&', '|', '<', '>', '='):
            self._eat()
            self.compile_term()
        self.result.append("</expression>")

    def compile_term(self) -> None:
        self.result.append("<term>")
        if self.tokenizer.get_token_type() in (TokenType.INT_CONST, TokenType.STRING_CONST, TokenType.KEYWORD):
            self._eat()
        elif self.tokenizer.get_token_type() == TokenType.IDENTIFIER:
            self._eat()
            if self.tokenizer.current_token == "[":
                self._eat("[")
                self.compile_expression()
                self._eat("]")
            elif self.tokenizer.current_token in ("(", "."):
                if self.tokenizer.current_token == ".":
                    self._eat(".")
                    self._eat()
                self._eat("(")
                self.compile_expression_list()
                self._eat(")")
        elif self.tokenizer.current_token == "(":
            self._eat("(")
            self.compile_expression()
            self._eat(")")
        elif self.tokenizer.current_token in ("-", "~"):
            self._eat()
            self.compile_term()
        self.result.append("</term>")

    def compile_expression_list(self) -> None:
        self.result.append("<expressionList>")
        if self.tokenizer.current_token != ")":
            self.compile_expression()
            while self.tokenizer.current_token == ",":
                self._eat(",")
                self.compile_expression()

        self.result.append("<remove>")
        self.result.append("</remove>")

        self.result.append("</expressionList>")
