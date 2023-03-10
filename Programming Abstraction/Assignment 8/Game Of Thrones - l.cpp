
// https://www.hackerrank.com/challenges/game-of-thrones/problem?isFullScreen=true

#include <bits/stdc++.h>

using namespace std;

/*
 * Complete the 'gameOfThrones' function below.
 *
 * The function is expected to return a STRING.
 * The function accepts STRING s as parameter.
 */

string gameOfThrones(string s) {
    int freq[26];
    for(int i = 0; i < 26; i++) {
        freq[i] = 0;
    }
    for(int i = 0; i < s.length(); i++) {
        freq[s[i] - 'a']++;
    }
    bool temp = true;
    for(int i = 0; i < 26; i++) {
        if(!temp && freq[i] % 2 != 0) return "NO";
        if(freq[i] % 2 == 1) temp = false;
    }
    return "YES";
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string s;
    getline(cin, s);

    string result = gameOfThrones(s);

    fout << result << "\n";

    fout.close();

    return 0;
}
