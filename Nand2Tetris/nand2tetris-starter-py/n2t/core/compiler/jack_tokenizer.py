import re
from pathlib import Path
from typing import Optional

from n2t.core.compiler import jack_constants
from n2t.core.compiler.token_types import TokenType


def remove_comments(path):
    text = Path(path).read_text(encoding="utf-8")
    text = re.sub(r'/\*.*?\*/', '', text, flags=re.DOTALL)

    lines = text.splitlines()
    clean_lines = []
    for line in lines:
        stripped = re.sub(r'//.*', '', line)
        if stripped.strip():
            clean_lines.append(stripped)
    text = '\n'.join(clean_lines)

    return text

def _tokenize_file(path: str) -> list[str]:
    text = remove_comments(path)
    pattern = r'"[^"\n]*"|[\{\}\(\)\[\]\.,;\+\-\*/&|<>=~]|\w+'
    return re.findall(pattern, text)


class JackTokenizer:
    def __init__(self, jack_file_path: str):
        self.tokens = _tokenize_file(jack_file_path)
        self.current_token: Optional[str] = None
        self.current_index = 0

    def has_more_tokens(self) -> bool:
        return self.current_index < len(self.tokens)

    def advance(self) -> None:
        if self.has_more_tokens():
            self.current_token = self.tokens[self.current_index]
            self.current_index += 1

    def get_token_type(self) -> TokenType:
        if self.current_token in jack_constants.KEYWORDS:
            return TokenType.KEYWORD
        elif self.current_token in jack_constants.SYMBOLS:
            return TokenType.SYMBOL
        elif self.current_token.isdigit():
            return TokenType.INT_CONST
        elif self.current_token.startswith('"') and self.current_token.endswith('"'):
            return TokenType.STRING_CONST
        else:
            return TokenType.IDENTIFIER

    def get_keywords(self) -> str:
        if self.get_token_type() == TokenType.KEYWORD:
            return self.current_token
        raise ValueError("Current token is not a keyword")

    def get_symbol(self) -> str:
        if self.get_token_type() == TokenType.SYMBOL:
            return self.current_token
        raise ValueError("Current token is not a symbol")

    def get_identifier(self) -> str:
        if self.get_token_type() == TokenType.IDENTIFIER:
            return self.current_token
        raise ValueError("Current token is not an identifier")

    def get_int_value(self) -> int:
        if self.get_token_type() == TokenType.INT_CONST:
            return int(self.current_token)
        raise ValueError("Current token is not an integer constant")

    def get_string_value(self) -> str:
        if self.get_token_type() == TokenType.STRING_CONST:
            return self.current_token.strip('"')
        raise ValueError("Current token is not a string constant")
