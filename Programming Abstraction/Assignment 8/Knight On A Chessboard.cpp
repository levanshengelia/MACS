
// https://www.hackerrank.com/challenges/knightl-on-chessboard/copy-from/276795834?isFullScreen=true

#include <bits/stdc++.h>

using namespace std;

string ltrim(const string &);
string rtrim(const string &);

/*
 * Complete the 'knightlOnAChessboard' function below.
 *
 * The function is expected to return a 2D_INTEGER_ARRAY.
 * The function accepts INTEGER n as parameter.
 */
void bfs(int x, int y, int n, int &moves) {
    queue<pair<int, int>> q;
    q.push({0, 0});
    vector<vector<bool>> visited(n, vector<bool>(n, false));
    while(!q.empty()) {
        int size = q.size();
        for(int i = 0; i < size; i++) {
            pair<int, int> curr = q.front();
            q.pop();
            if(min(curr.first, curr.second) < 0 ||
                max(curr.first, curr.second) >= n) continue;
            if(visited[curr.first][curr.second]) continue;
            visited[curr.first][curr.second] = true;
            cout << curr.first << "," << curr.second << endl;
            if(curr.first == n - 1 && curr.second == n - 1) return;
            q.push({curr.first - x, curr.second - y});
            q.push({curr.first + x, curr.second - y});
            q.push({curr.first - x, curr.second + y});
            q.push({curr.first + y, curr.second + x});
            q.push({curr.first - y, curr.second - x});
            q.push({curr.first + y, curr.second - x});
            q.push({curr.first - y, curr.second + x});
        }
        moves++;
    }
    moves = -1;
}

vector<vector<int>> knightlOnAChessboard(int n) {
    vector<vector<int>> result(n - 1, vector<int>());
    for(int x = 1; x < n; x++) {
        for(int y = 1; y < n; y++) {
            int moves = 0;
            bfs(x, y, n, moves);
            result[x - 1].push_back(moves);
        }
    }
    return result;
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string n_temp;
    getline(cin, n_temp);

    int n = stoi(ltrim(rtrim(n_temp)));

    vector<vector<int>> result = knightlOnAChessboard(n);

    for (size_t i = 0; i < result.size(); i++) {
        for (size_t j = 0; j < result[i].size(); j++) {
            fout << result[i][j];

            if (j != result[i].size() - 1) {
                fout << " ";
            }
        }

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
