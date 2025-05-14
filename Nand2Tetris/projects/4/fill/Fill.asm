// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, 
// the screen should be cleared.

// if(KEYBOARD > 0) {
//     for(var i in SCREEN) {
//          i = -1
//     }
// }

@8192
D=A
@REGS_IN_SCREEN
M=D

(INFINITE_LOOP)

// if(Keyboard == 0) jump to NOT_PRESSED
@KBD
D=M
@NOT_PRESSED
D;JEQ

(Pressed)
//counter=0
@counter
M=0

(LOOP)

//Screen[counter] = -1
@counter
D=M
@SCREEN
A=A+D
M=-1

// counter++
@counter
M=M+1

// if(counter < 8192) jump to LOOP
@counter
D=M
@REGS_IN_SCREEN
D=D-M
@LOOP
D;JLT // 8192 is the number of registers in Screen

@INFINITE_LOOP
0;JMP

(NOT_PRESSED)
//counter=0
@counter
M=0

(LOOP2)

//Screen[counter] = -1
@counter
D=M
@SCREEN
A=A+D
M=0

// counter++
@counter
M=M+1

// if(counter < 8192) jump to LOOP
@counter
D=M
@REGS_IN_SCREEN
D=D-M
@LOOP2
D;JLT // 8192 is the number of registers in Screen

@INFINITE_LOOP
0;JMP

@INFINITE_LOOP
0;JMP