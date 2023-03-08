import sys, re, copy
from functools import reduce
from fractions import Fraction

def tokenise(txt):
    tokens = []
    token = None

    for c in txt:
        if c.isspace():
            if token is not None:
                tokens.append(token)
                token = None
        elif c == '(' or c == ')' or c == '\n':
            if token is not None:
                tokens.append(token)
                token = None
            tokens.append(c)
        else:
            if token is None:
                token = ''
            token += c

    if token is not None:
        tokens.append(token)

    return [t for t in tokens if t is not None]

def parse(tokens):
    stack = [[]]
    for token in tokens:
        if token == '(':
            stack.append([])
        elif token == ')':
            sublist = stack.pop()
            stack[-1].append(sublist)
        else:
            try:
                value = float(token)
            except ValueError:
                value = token
            stack[-1].append(value)
    return stack[0]

def addition(token, defineVars):
    sum = 0
    for i in range(1, len(token)):
        sum += evaluation(token[i], defineVars)
    return sum

def subtract(token, defineVars):
    if(len(token) == 2):
        return -evaluation(token[1], defineVars)
    res = token[1]
    for i in range(2, len(token)):
        res -= evaluation(token[i], defineVars)
    return res

def multiply(token, defineVars):
    res = 1
    for i in range(1, len(token)):
        res *= evaluation(token[i], defineVars)
    return res

def getFraction(num, denom):
    if int(num) == num and int(denom) == denom:
        return Fraction(int(num), int(denom))
    else:
        return num / denom
    
def division(token, defineVars):
    if len(token) == 2:
        return getFraction(1, evaluation(token[1], defineVars))
    num = evaluation(token[1], defineVars)
    denom = 1
    for i in range (2, len(token)):
        denom *= evaluation(token[i], defineVars)
    return getFraction(num, denom)

def cons(token, defineVars):
    res = evaluation(token[2], defineVars)
    res.insert(0, evaluation(token[1], defineVars))
    return res

def car(token, defineVars):
    return evaluation(token[1], defineVars)[0]

def cdr(token, defineVars):
    return evaluation(token[1], defineVars)[1:]

def append(token, defineVars):
    return evaluation(token[1], defineVars) + evaluation(token[2], defineVars)

def apply(token, defineVars):
    res = evaluation(token[2], defineVars)
    res.insert(0, token[1])
    return evaluation(res, defineVars)

def null(token):
    return len(token[1]) == 0

def length(token, defineVars):
    return len(evaluation(token[1], defineVars))

def ifStatement(token, defineVars):
    statement = evaluation(token[1], defineVars)
    if statement:
        return evaluation(token[2], defineVars)
    else:
        return evaluation(token[3], defineVars)

def equals(token, defineVars):
    return evaluation(token[1], defineVars) == evaluation(token[2], defineVars)

def greater(token, defineVars):
    return evaluation(token[1], defineVars) > evaluation(token[2], defineVars)

def less(token, defineVars):
    return evaluation(token[1], defineVars) < evaluation(token[2], defineVars)

def greaterEqual(token, defineVars):
    return evaluation(token[1], defineVars) >= evaluation(token[2], defineVars)

def lessEqual(token, defineVars):
    return evaluation(token[1], defineVars) <= evaluation(token[2], defineVars)

def notStatement(token, defineVars):
    statement = evaluation(token[1], defineVars)
    return not statement

def andStatement(token, defineVars):
    return evaluation(token[1], defineVars) and evaluation(token[2], defineVars)

def orStatement(token, defineVars):
    return evaluation(token[1], defineVars) or evaluation(token[2], defineVars)

def replace(func, parameter, newElem, defineVars):
    for i in range(len(func)):
        if func[i] == parameter:
            func[i] = newElem
        elif isinstance(func[i], list):
            func[i] = replace(func[i], parameter, newElem, defineVars)
    return func

def mapFun(token, defineVars):
    if token[1][0] == 'lambda':
        parameterList = token[2]
        parameter = token[1][1][0]
        res = []
        for i in range(len(parameterList)):
            func = copy.deepcopy(token[1][2])
            func = replace(func, parameter, parameterList[i])
            res.append(evaluation(func), defineVars)
        return res
    else:
        res = []
        expr = []
        expr.append(token[1][0])
        parameterList = token[2]
        for i in range(len(parameterList)):
            expr.append(parameterList[i])
            res.append(evaluation(expr, defineVars))
            del expr[1]
        return res

def lambdaFunc(token, defineVars):
    expr = copy.deepcopy(token[0][2])
    for i in range(len(token[0][1])):
        expr = replace(expr, token[0][1][i], token[i + 1])
    return evaluation(expr, defineVars)

def define(token, defineVars):
    if not isinstance(token[1], list):
        defineVars[token[1]] = token[2]
    else:
        pair = []
        pair.append(token[1][1:])
        pair.append(token[2])
        defineVars[token[1][0]] = pair

def definedOperation(operation, defineVars):
    operation[1] = evaluation(operation[1], defineVars)
    pair = defineVars[operation[0]]
    parameters = []
    temp = pair[1].copy()
    for i in operation[1:]:
        parameters.append(evaluation(i, defineVars))
    for i in range(len(pair[0])):
        replace(temp, pair[0][i], parameters[i], defineVars)
    res = evaluation(temp, defineVars)
    return res
    
def evaluation(token, defineVars):
    if (not isinstance(token, list)) and token in defineVars:
        return evaluation(defineVars[token], defineVars)
    elif token == '#t':
        return True
    elif token == '#f':
        return False
    elif isinstance(token, list) and isinstance(token[0], list) and token[0][0] == 'lambda':
        return lambdaFunc(token)
    elif not isinstance(token, list) or isinstance(token[0], float) or isinstance(token[0], list):
        return token
    elif token[0] == '+':
        return addition(token, defineVars)
    elif token[0] == '-':
        return subtract(token, defineVars)
    elif token[0] == '*':
        return multiply(token, defineVars)
    elif token[0] == '/':
        return division(token, defineVars)
    elif token[0] == "'":
        return token[1:]
    elif token[0] == 'cons':
        return cons(token, defineVars)
    elif token[0] == 'car':
        return car(token, defineVars)
    elif token[0] == 'cdr':
        return cdr(token, defineVars)
    elif token[0] == 'append':
        return append(token, defineVars)
    elif token[0] == 'apply':
        return apply(token, defineVars)
    elif token[0] == 'null?':
        return null(token)
    elif token[0] == 'length':
        return length(token, defineVars)
    elif token[0] == 'if':
        return ifStatement(token, defineVars)
    elif token[0] == '=':
        return equals(token, defineVars)
    elif token[0] == '>':
        return greater(token, defineVars)
    elif token[0] == '<':
        return less(token, defineVars)
    elif token[0] == '>=':
        return greaterEqual(token, defineVars)
    elif token[0] == '<=':
        return lessEqual(token, defineVars)
    elif token[0] == 'not':
        return notStatement(token, defineVars)
    elif token[0] == 'and':
        return andStatement(token, defineVars)
    elif token[0] == 'or':
        return orStatement(token, defineVars)
    elif token[0] == 'map':
        return mapFun(token, defineVars)
    elif token[0] == 'define':
        return define(token, defineVars)
    elif token[0] in defineVars:
        return definedOperation(token, defineVars)

def printSchemeList(res):
    print('(', end = '')
    for i in range(len(res)):
        if i != 0:
            print(' ', end = '')
        if(isinstance(res[i], float)):
            print(getDecimal(res[i]), end = '')
        elif(isinstance(res[i], list)):
            printSchemeList(res[i])
        elif(isinstance(res[i], str)):
            print(res[i], end = '')
        elif(isinstance(res[i], Fraction)):
            print(res[i], end = '')
    print(')', end = '')

def getDecimal(res):
    if int(res) == res:
        return int(res)
    else:
        return res

def printBool(res):
    if res:
        print('#t')
    else:
        print('#f')

def printAnswer(res):
    if isinstance(res, bool):
        printBool(res)
    elif isinstance(res, int):
        print(res)
    elif isinstance(res, float):
        print(getDecimal(res)) 
    elif isinstance(res, list):
        printSchemeList(res)
        print()
    elif isinstance(res, str):
        print(res)
    elif isinstance(res, Fraction):
        print(res)
    
def removeApostrophes(tokens):
    for i in range(len(tokens)):
        if tokens[i] == "'":
            del tokens[i]
            return removeApostrophes(tokens)
        if isinstance(tokens[i], list):
            tokens[i] = removeApostrophes(tokens[i])
    return tokens

def processData(tokens, defineVars):
    tokens = removeApostrophes(tokens)
    for i in range(len(tokens)):
        printAnswer(evaluation(tokens[i], defineVars))

if __name__ == '__main__':
    tokens = tokenise(open("scheme.txt").read())
    tokens = parse(tokens)
    defineVars = {}
    processData(tokens, defineVars)
