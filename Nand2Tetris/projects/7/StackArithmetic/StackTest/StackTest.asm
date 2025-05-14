// push constant 17
@17
D=A
@SP
A=M
M=D
@0
M=M+1
// push constant 17
@17
D=A
@SP
A=M
M=D
@0
M=M+1
// eq
@SP
AM=M-1
D=M
A=A-1
D=M-D
M=-1
@END2
D;JEQ
@SP
A=M-1
M=0
(END2)
// push constant 17
@17
D=A
@SP
A=M
M=D
@0
M=M+1
// push constant 16
@16
D=A
@SP
A=M
M=D
@0
M=M+1
// eq
@SP
AM=M-1
D=M
A=A-1
D=M-D
M=-1
@END5
D;JEQ
@SP
A=M-1
M=0
(END5)
// push constant 16
@16
D=A
@SP
A=M
M=D
@0
M=M+1
// push constant 17
@17
D=A
@SP
A=M
M=D
@0
M=M+1
// eq
@SP
AM=M-1
D=M
A=A-1
D=M-D
M=-1
@END8
D;JEQ
@SP
A=M-1
M=0
(END8)
// push constant 892
@892
D=A
@SP
A=M
M=D
@0
M=M+1
// push constant 891
@891
D=A
@SP
A=M
M=D
@0
M=M+1
// lt
@SP
AM=M-1
D=M
A=A-1
D=M-D
M=-1
@END11
D;JLT
@SP
A=M-1
M=0
(END11)
// push constant 891
@891
D=A
@SP
A=M
M=D
@0
M=M+1
// push constant 892
@892
D=A
@SP
A=M
M=D
@0
M=M+1
// lt
@SP
AM=M-1
D=M
A=A-1
D=M-D
M=-1
@END14
D;JLT
@SP
A=M-1
M=0
(END14)
// push constant 891
@891
D=A
@SP
A=M
M=D
@0
M=M+1
// push constant 891
@891
D=A
@SP
A=M
M=D
@0
M=M+1
// lt
@SP
AM=M-1
D=M
A=A-1
D=M-D
M=-1
@END17
D;JLT
@SP
A=M-1
M=0
(END17)
// push constant 32767
@32767
D=A
@SP
A=M
M=D
@0
M=M+1
// push constant 32766
@32766
D=A
@SP
A=M
M=D
@0
M=M+1
// gt
@SP
AM=M-1
D=M
A=A-1
D=M-D
M=-1
@END20
D;JGT
@SP
A=M-1
M=0
(END20)
// push constant 32766
@32766
D=A
@SP
A=M
M=D
@0
M=M+1
// push constant 32767
@32767
D=A
@SP
A=M
M=D
@0
M=M+1
// gt
@SP
AM=M-1
D=M
A=A-1
D=M-D
M=-1
@END23
D;JGT
@SP
A=M-1
M=0
(END23)
// push constant 32766
@32766
D=A
@SP
A=M
M=D
@0
M=M+1
// push constant 32766
@32766
D=A
@SP
A=M
M=D
@0
M=M+1
// gt
@SP
AM=M-1
D=M
A=A-1
D=M-D
M=-1
@END26
D;JGT
@SP
A=M-1
M=0
(END26)
// push constant 57
@57
D=A
@SP
A=M
M=D
@0
M=M+1
// push constant 31
@31
D=A
@SP
A=M
M=D
@0
M=M+1
// push constant 53
@53
D=A
@SP
A=M
M=D
@0
M=M+1
// add
@SP
AM=M-1
D=M
A=A-1
M=M+D
// push constant 112
@112
D=A
@SP
A=M
M=D
@0
M=M+1
// sub
@SP
AM=M-1
D=M
A=A-1
M=M-D
// neg
@SP
A=M-1
M=-M
// and
@SP
AM=M-1
D=M
A=A-1
M=M&D
// push constant 82
@82
D=A
@SP
A=M
M=D
@0
M=M+1
// or
@SP
AM=M-1
D=M
A=A-1
M=M|D
// not
@SP
A=M-1
M=!M
