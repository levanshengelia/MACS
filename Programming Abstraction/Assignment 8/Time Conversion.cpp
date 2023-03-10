
// https://www.hackerrank.com/challenges/time-conversion/copy-from/225337936?isFullScreen=true

#include <bits/stdc++.h>

using namespace std;

/*
 * Complete the 'timeConversion' function below.
 *
 * The function is expected to return a STRING.
 * The function accepts STRING s as parameter.
 */

string timeConversion(string s) {
    int hours = stoi(s.substr(0, 2));
        string dayTime = s.substr(8);
        if(dayTime == "AM" && hours != 12) {
            return s.substr(0, 8);
        } else if (dayTime == "AM" && hours == 12) {
            return "00" + s.substr(2, 6);
        } else if(dayTime == "PM" && hours == 12) {
            return s.substr(0, 8);
        } else {
            return string("" + to_string(hours + 12) + s.substr(2, 6));
        }
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string s;
    getline(cin, s);

    string result = timeConversion(s);

    fout << result << "\n";

    fout.close();

    return 0;
}
