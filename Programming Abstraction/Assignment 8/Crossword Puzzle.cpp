
// https://www.hackerrank.com/challenges/crossword-puzzle/problem?isFullScreen=true

#include <bits/stdc++.h>

using namespace std;

string ltrim(const string &);
string rtrim(const string &);

/*
 * Complete the 'crosswordPuzzle' function below.
 *
 * The function is expected to return a STRING_ARRAY.
 * The function accepts following parameters:
 *  1. STRING_ARRAY crossword
 *  2. STRING words
 */
 
void delimitTheWords(string words, vector<string> & vec) {
    int index = 0;
    int chars = 0;
    for(int i = 0; i < words.length(); i++) {
        if(words[i] == ';') {
            vec.push_back(words.substr(index, chars));
            chars = 0;
            index = i + 1;
        } else {
            chars++;
        }
    }
    vec.push_back(words.substr(index));
}

void getFirstHorizontal(vector<string> &crossword, int &row, int &col) {
    for(int i = 0; i < crossword.size(); i++) {
        for(int j = 0; j < crossword[i].length(); j++) {
            if(crossword[i][j] == '-' && j + 1 < crossword[i].length() - 1 &&
                crossword[i][j + 1] == '-') {
                row = i;
                col = j;
                return;
            }
        }
    }
}

bool isSolved(vector<string> &crossword) {
    for(int i = 0; i < crossword.size(); i++) {
        for(int j = 0; j < crossword[i].length(); j++) {
            if(crossword[i][j] == '-') {
                return false;
            }
        }
    }
    return true;
}

int getHorizontalSize(vector<string> &crossword, int row, int col) {
    int size = 0;
    for(int i = col; i < crossword[row].length(); i++) {
        if(crossword[row][i] == '+') break;
        if(crossword[row][i] == '-' || isalpha(crossword[row][i])) {
            size++;
        }
    }
    return size;
}

int getVerticalSize(vector<string> &crossword, int row, int col) {
    int size = 0;
    for(int i = row; i < crossword.size(); i++) {
        if(crossword[i][col] == '+') break;
        if(crossword[i][col] == '-' || isalpha(crossword[i][col])) {
            size++;
        }
    }
    return size;
}

bool appendVertical(vector<string> &crossword, int row, int col, string word) {
    for(int i = row; i < row + word.length(); i++) {
        if(crossword[i][col] == '-' || crossword[i][col] == word[i - row]) {
            crossword[i][col] = word[i - row];
        } else {
            return false;
        }
    }
    return true;
}

bool appendHorizontal(vector<string> &crossword, int row, int col, string word) {
    for(int i = col; i < col + word.length(); i++) {
        if(crossword[row][i] == '-' || crossword[row][i] == word[i - col]) {
            crossword[row][i] = word[i - col];
        } else {
            return false;
        }
    }
    return true;
}

void getFirstVertical(vector<string> &crossword, int &row, int &col) {
    for(int i = 0; i < crossword.size(); i++) {
        for(int j = 0; j < crossword[i].length(); j++) {
            if(i + 1 >= crossword.size() - 1) continue;
            char c1 = crossword[i][j];
            char c2 = crossword[i + 1][j];
            if((c1 == '-' && c2 == '-') || (c1 == '-' && isalpha(c2)) || (isalpha(c1) && c2 == '-')) {
                row = i;
                col = j;
                return;
            }
        }
    }
}

void DFS(vector<string> &crossword, vector<string> &words, vector<string> &res, bool solve = false) {
    if(isSolved(crossword)) {
        res = crossword;
        solve = true;
        cout << "base case" << endl;
    } else {
        int row = -1;
        int col = -1;
        getFirstHorizontal(crossword, row, col);
        if(row == -1) {
            getFirstVertical(crossword, row, col);
            vector<string> copy = crossword;
            int size = getVerticalSize(crossword, row, col);
            for(int i = 0; i < words.size(); i++) {
                if(words[i].length() != size) continue;
                string word = words[i];
                if(appendVertical(crossword, row, col, word)) {
                    words.erase(words.begin() + i);
                    DFS(crossword, words, res, solve);
                    if(solve) return;
                    words.insert(words.begin() + i, word);
                }
                crossword = copy;
            }
        } else {
            vector<string> copy = crossword;
            int size = getHorizontalSize(crossword, row, col);
            for(int i = 0; i < words.size(); i++) {
                if(words[i].length() != size) continue;
                string word = words[i];
                if(appendHorizontal(crossword, row, col, word)) {
                    words.erase(words.begin() + i);
                    DFS(crossword, words, res, solve);
                    if(solve) return;
                    words.insert(words.begin() + i, word);
                }
                crossword = copy;
            }
        }
    }
}

vector<string> crosswordPuzzle(vector<string> crossword, string words) {
    vector<string> vec;
    delimitTheWords(words, vec);
    vector<string> res;
    DFS(crossword, vec, res);
    return res;
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    vector<string> crossword(10);

    for (int i = 0; i < 10; i++) {
        string crossword_item;
        getline(cin, crossword_item);

        crossword[i] = crossword_item;
    }

    string words;
    getline(cin, words);

    vector<string> result = crosswordPuzzle(crossword, words);

    for (size_t i = 0; i < result.size(); i++) {
        fout << result[i];

        if (i != result.size() - 1) {
            fout << "\n";
        }
    }

    fout << "\n";

    fout.close();

    return 0;
}

string ltrim(const string &str) {
    string s(str);

    s.erase(
        s.begin(),
        find_if(s.begin(), s.end(), not1(ptr_fun<int, int>(isspace)))
    );

    return s;
}

string rtrim(const string &str) {
    string s(str);

    s.erase(
        find_if(s.rbegin(), s.rend(), not1(ptr_fun<int, int>(isspace))).base(),
        s.end()
    );

    return s;
}
