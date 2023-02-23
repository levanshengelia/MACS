/*
 * File: Boggle.cpp
 * ----------------
 * Name: [TODO: Levan Shengelia]
 * Section: [TODO: Ruska Keldishvili]
 * This file is the main starter file for Assignment #4, Boggle.
 * [TODO: extend the documentation]
 */
#include <iostream>
#include "gboggle.h"
#include "grid.h"
#include "gwindow.h"
#include "lexicon.h"
#include "random.h"
#include "simpio.h"
#include <cctype>
#include <utility>
using namespace std;

/* Constants */
const int BOGGLE_WINDOW_WIDTH = 650;
const int BOGGLE_WINDOW_HEIGHT = 350;

const string STANDARD_CUBES[16]  = {
    "AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
    "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
    "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"
};
 
const string BIG_BOGGLE_CUBES[25]  = {
    "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
    "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW",
    "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT",
    "DHHLOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
    "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"
};

/* Function prototypes */
void welcome();
void giveInstructions();
void play();
void set_game(Grid<char> & cubes);
void set_configuration(Grid<char> & cubes, int size);
void set_custom_configuration(Grid<char> & cubes, int size);
void set_default_configuration(Grid<char> & cubes, int size);
void draw_cubes(Grid<char> & cubes);
void player_move(Grid<char> & cubes, Lexicon & lexicon);
void fill_grid(string configuration, Grid<char> & cubes);
bool is_valid_guess(Grid<char> & cubes, string guess, Set<pair<int, int> > & visited,
					Lexicon & lexicon, int row, int col, int index);
void computer_move(Grid<char> & cubes, Lexicon & lexicon);
void find_words(Grid<char> & cubes, Set<string> & foundWords, int row, int col, 
				Set<pair<int, int> > & visited, Lexicon & lexicon, string word);
void highlight(Grid<char> & cubes, Set<pair<int, int> > & visited);


/* Main program */
int main() {
    GWindow gw(BOGGLE_WINDOW_WIDTH, BOGGLE_WINDOW_HEIGHT);
    initGBoggle(gw);
    welcome();
	while(true) {
		string str = getLine("Do you need instructrions? ");
		if(toLowerCase(str) == "yes") {
			giveInstructions();
			break;
		} else if(toLowerCase(str) == "no") {
			break;
		}
		cout << "Please answer yes or no" << endl;
	}
	while(true) {
		play();
		string str = getLine("Would you like to play again? ");
		if(str == "no") break;
		if(str == "yes") continue;
		cout << "Please answer yes or no" << endl;
	}
    return 0;
}

/*
 * Function: welcome
 * Usage: welcome();
 * -----------------
 * Print out a cheery welcome message.
 */

void welcome() {
    cout << "Welcome!  You're about to play an intense game ";
    cout << "of mind-numbing Boggle.  The good news is that ";
    cout << "you might improve your vocabulary a bit.  The ";
    cout << "bad news is that you're probably going to lose ";
    cout << "miserably to this little dictionary-toting hunk ";
    cout << "of silicon.  If only YOU had a gig of RAM..." << endl << endl;
}

/*
 * Function: giveInstructions
 * Usage: giveInstructions();
 * --------------------------
 * Print out the instructions for the user.
 */

void giveInstructions() {
    cout << endl;
    cout << "The boggle board is a grid onto which I ";
    cout << "I will randomly distribute cubes. These ";
    cout << "6-sided cubes have letters rather than ";
    cout << "numbers on the faces, creating a grid of ";
    cout << "letters on which you try to form words. ";
    cout << "You go first, entering all the words you can ";
    cout << "find that are formed by tracing adjoining ";
    cout << "letters. Two letters adjoin if they are next ";
    cout << "to each other horizontally, vertically, or ";
    cout << "diagonally. A letter can only be used once ";
    cout << "in each word. Words must be at least four ";
    cout << "letters long and can be counted only once. ";
    cout << "You score points based on word length: a ";
    cout << "4-letter word is worth 1 point, 5-letters ";
    cout << "earn 2 points, and so on. After your puny ";
    cout << "brain is exhausted, I, the supercomputer, ";
    cout << "will find all the remaining words and double ";
    cout << "or triple your paltry score." << endl << endl;
    cout << "Hit return when you're ready...";
    getLine();
}

/*
* Function: play
* Usage: play();
* --------------
* The playing process of the game
*/
void play() {
	Grid<char> cubes;
	Lexicon lexicon("EnglishWords.dat");
	draw_cubes(cubes);
	player_move(cubes, lexicon);
	computer_move(cubes, lexicon);
}

/*
* Function: set_game
* Usage: set_game(cubes);
* -----------------------
* Functions takes grid as a parameter and writes random character of entered cube
* in each cell. This function allows player to choose game settings.
*/
void set_game(Grid<char> & cubes) {
	cout << "You can choose standard Boggle (4x4 grid)" << endl;
	cout << "or Big Boggle(5x5)." << endl;
	while(true) {
		string str = getLine("Would you like Big Boggle? ");
		if(toLowerCase(str) == "yes") {
			set_configuration(cubes, 5);
			break;
		} else if(toLowerCase(str) == "no") {
			set_configuration(cubes, 4);
			break;
		}
		cout << "Please answer yes or no" << endl;
	}
}
/*
* Function: set_configuration
* Usage: set_configuration(cubes, 4);
* -----------------------------------
* This function draws size x size board and sets its configuration
*/
void set_configuration(Grid<char> & cubes, int size) {
	cubes = Grid<char>(size, size);
	drawBoard(size, size);
	cout << "I'll give you a chance to set up the board to your specification " << endl; 
	cout << "which makes it easier to confirm your boggle program is working. " << endl;
	while(true) {
		string str = getLine("Do you want to force the board configuration? ");
		if(toLowerCase(str) == "yes") {
			set_custom_configuration(cubes, size);
			break;
		} else if(toLowerCase(str) == "no") {
			set_default_configuration(cubes, size);
			break;
		}
		cout << "Please answer yes or no" << endl;
	}
}
/*
* Function: set_custom_configuration
* Usage: set_custom_configuraton(cubes, size);
* --------------------------------------
* This function allows player to set configuration of cubes
*/
void set_custom_configuration(Grid<char> & cubes, int size) {
	cout << "Enter a " + integerToString(size * size) + "-character string to identify which letters you want on the cubes.";
	cout << "The first 5 letters are the cubes on the top row from left to right, the next 5 letters are the second row, and so on." << endl;
	while(true) {
		string configuration = getLine("Enter the string: ");
		if(configuration.length() >= size * size) {
			fill_grid(configuration.substr(0, size * size), cubes);
			break;
		}
		cout << "String must include " + integerToString(size * size) + " characters! Try again." << endl;
	}
}
/*
* Function: fill_grid
* Usage: fill_grid(configuration, cubes);
* ---------------------------------------
* Filling the grid with letters which are in player's string
*/

void fill_grid(string configuration, Grid<char> & cubes) {
	configuration = toUpperCase(configuration);
	int index = 0;
	for(int i = 0; i < cubes.nRows; i++) {
		for(int j = 0; j < cubes.nCols; j++) {
			cubes[i][j] = configuration[index];
			index++;
		}
	}
	draw_cubes(cubes);
}

/*
* Function: set_default_configuration;
* Usage: set_default_configuration(cubes, size);
* ----------------------------------------------
* This function fills grid with default configuration in case when player decides not
* to enter the custom configuration. The default configuration is STANDARD_CUBES(4x4) 
* or BIG_BOGGLE_CUBES(5x5).
*/
void set_default_configuration(Grid<char> & cubes, int size) {
	if(size == 4) {
		for(int i = 0; i < 16; i++) {
			cubes[i / size][i % size] = STANDARD_CUBES[i][randomInteger(0,5)];
		}
	} else {
		for(int i = 0; i < 25; i++) {
			cubes[i / size][i % size] = BIG_BOGGLE_CUBES[i][randomInteger(0,5)];
		}
	}
	draw_cubes(cubes);
}

/*
* Function: draw_cubes
* Usage: draw_cubes(cubes);
* -------------------------
* This functions takes grid of cubes as a parameter and draws it on canvas
* using gboggle.h's functions
*/
void draw_cubes(Grid<char> & cubes) {
	for(int row = 0; row < cubes.nRows; row++) {
		for(int col = 0; col < cubes.nCols; col++) {
			labelCube(row, col, cubes[row][col]);
		}
	}
}

/*
* Function: player_move
* Usage: player_move(cubes);
* --------------------------
* This function represents player's move. Player is trying to guess as many 
* words as possible.
*/
void player_move(Grid<char> & cubes, Lexicon & lexicon) {
	Set<string> guessedWords;
	while(true) {
		string guess = toUpperCase(getLine("enter a word: "));

		if(guess == "") break;
		if(guessedWords.contains(guess)) {
			cout << "You've already guessed that!" << endl;
			continue;
		}
		if(guess.length() < 4) {
			cout << "I'm sorry but we have our standards." << endl;
			cout << "That word doesn't meet the minimum word length." << endl;
		} else if(!lexicon.contains(guess)) {
			cout << "That's not a word" << endl;
		} else {
			Set<pair<int, int> > visited;
			bool is_guess_correct = false;
			for(int i = 0; i < cubes.nRows; i++) {
				for(int j = 0; j < cubes.nCols; j++) {
					if(cubes[i][j] == guess[0]) {
						if(is_valid_guess(cubes, guess, visited, lexicon, i, j, 1)) {
							recordWordForPlayer(guess, HUMAN);
							highlight(cubes, visited);
							is_guess_correct = true;
							guessedWords += guess;
							visited.clear();
							break;
						}
					}
				}
				if(is_guess_correct) break;
			}
			if(is_guess_correct) continue;
			cout << "You can't make that word!" << endl;
		}
	}
}

/*
* Funtion: is_valid_guess
* Usage: if(is_valid_guess(cubes, guess, visited, lexicon, row, col, word))
* -------------------------------------------------------------------
* This function return true if passed string can be composed with letters in the 
* grid which are neighbours of each other horizontally, vertically or diagonally.
*/
bool is_valid_guess(Grid<char> & cubes, string guess, Set<pair<int, int> > & visited,
					Lexicon & lexicon, int row, int col, int index) {
	pair<int, int> p(row, col);
	visited += p;
	if(index >= guess.length()) {
		return true;
	} else {
		Set<pair<int, int> > copy = visited;
		for(int dRow = -1; dRow <= 1; dRow++) {
			for(int dCol = -1; dCol <= 1; dCol++) {
				pair<int, int> location(row + dRow, col + dCol);
				if(cubes.inBounds(location.first, location.second) && !visited.contains(location)
					&& cubes[location.first][location.second] == guess[index]
					&& is_valid_guess(cubes, guess, visited, lexicon, location.first, 
					location.second, index + 1)) {
					return true;
				}
				visited = copy;
			}
		}
		return false;
	}
}


/*
* Function: computer_move
* Usage: computer_move(cubes);
* ----------------------------
* This function represents computer's move. Computer finds all the possible words that 
* can be composed with letters in the cubes.
*/
void computer_move(Grid<char> & cubes, Lexicon & lexicon) {
	Set<string> foundWords;
	for(int row = 0; row < cubes.nRows; row++) {
		for(int col = 0; col < cubes.nCols; col++) {
			Set<pair<int, int> > visited;
			find_words(cubes, foundWords, row, col, visited, lexicon, "");
		}
	}
}

/*
* Function: find_words
* Usage: find_words(cubes, foundWords, row, col, visited, lexicon, word);
* -----------------------------------------------
* This function recursively finds all the possible words which can be composed 
* from (row x col) starting point.
*/

void find_words(Grid<char> & cubes, Set<string> & foundWords, int row, int col, 
				Set<pair<int, int> > & visited, Lexicon & lexicon, string word) {
	pair<int, int> location(row, col);
	visited += location;
	Set<pair<int, int> > copy = visited;
	word += cubes[row][col];
	if(!lexicon.containsPrefix(word)) return;
	if(word.length() >= 4 && lexicon.contains(word) && !foundWords.contains(word)) {
		recordWordForPlayer(word, COMPUTER);
		foundWords += word;
	}
	for(int dRow = -1; dRow <= 1; dRow++) {
		for(int dCol = -1; dCol <= 1; dCol++) {
			pair<int, int> p(row + dRow, col + dCol);
			if(cubes.inBounds(p.first, p.second) && !visited.contains(p)) {
				find_words(cubes, foundWords, p.first, p.second, visited, lexicon, word);
				visited = copy;
			}
		}
	}
}

/*
* Function: highlight
* Usage: highlight(cubes, visited);
* ---------------------------------
* This function highlights the cells of the grid which are the letters of the guessed word
*/
void highlight(Grid<char> & cubes, Set<pair<int, int> > & visited) {
	while(!visited.isEmpty()) {
		pair<int, int> p = visited.first();
		visited -= p;
		highlightCube(p.first, p.second, true);
	}
	pause(300);
	for(int i = 0; i < cubes.nRows; i++) {
		for(int j = 0; j < cubes.nCols; j++) {
			highlightCube(i, j, false);
		}
	}
}