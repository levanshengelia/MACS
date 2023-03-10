
// https://www.hackerrank.com/challenges/sherlock-and-valid-string/copy-from/276716708?isFullScreen=true

#include <bits/stdc++.h>

using namespace std;

/*
 * Complete the 'isValid' function below.
 *
 * The function is expected to return a STRING.
 * The function accepts STRING s as parameter.
 */
void getCharFrequency(string s, vector<int> &arr) {
    for (int i = 0; i < s.length(); i++) {
        arr[s[i] - 'a']++;
    }
}

string isValid(string s) {
        vector<int> arr(26, 0); 
        getCharFrequency(s, arr);
        sort(arr.begin(), arr.end());
        int index = 0;
        while(true) {
            if (arr[index] == 0) {
                index++;
            } else {
                break;
            }
        }
        bool p = false;
        for (int i = index; i < arr.size() - 1; i++) {
            if (arr[i] != arr[i + 1]) {
                if (i == index && arr[i] == 1) {
                    p = true;
                    continue;
                }
                if (p) {
                    return "NO";
                } else {
                    arr[i + 1]--;
                    if (arr[i + 1] != arr[i]) {
                        return "NO";
                    }
                    p = true;
                }
            }
        }
        return "YES";
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string s;
    getline(cin, s);

    string result = isValid(s);

    fout << result << "\n";

    fout.close();

    return 0;
}
