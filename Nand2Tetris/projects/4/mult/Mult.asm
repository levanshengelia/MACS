// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
// The algorithm is based on repetitive addition.

//// Replace this comment with your code.

// r[2] = 0
// for(i = 0; i < ram[1]; i++) {
//      r[2] += r[0] 
// }

//RAM[2] = 0
@R2
M=0

//counter = RAM[1]
@R1
D=M
@counter
M=D
D=M

@END
D;JEQ


(LOOP)
// RAM[2] += RAM[0]
@R0
D=M
@R2
M=M+D

//counter--
@counter
D=M-1;
M=D

//if(counter > 0) jump to LOOP
@LOOP
D;JGT

(END)
@END
0;JMP
