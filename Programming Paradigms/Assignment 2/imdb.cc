using namespace std;
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <unistd.h>
#include "imdb.h"
 #include <cstring> 

const char *const imdb::kActorFileName = "actordata";
const char *const imdb::kMovieFileName = "moviedata";

imdb::imdb(const string& directory)
{
  const string actorFileName = directory + "/" + kActorFileName;
  const string movieFileName = directory + "/" + kMovieFileName;
  
  actorFile = acquireFileMap(actorFileName, actorInfo);
  movieFile = acquireFileMap(movieFileName, movieInfo);
}

struct key_movie {
  void* base;
  string name;
  int year;
};

struct key_player {
  void* base;
  string name;
};

bool imdb::good() const
{

  return !( (actorInfo.fd == -1) || 
	    (movieInfo.fd == -1) ); 
}

// compare function of players for bsearch
int compare_players(const void* player1, const void* player2) {
  key_player player_info = *(key_player*)player1;
  void* base = player_info.base;
  char* name1 = (char *)(player_info.name.c_str());
  char* name2 = (char *)base + *(int*)player2;
  int res = strcmp(name1, name2);
  return res;
}

// this function fills the list with films in which particular actor/actress played
bool imdb::getCredits(const string& player, vector<film>& films) const { 

  // defining some variables while we have an access to actorFile
  int* base = (int*)actorFile + 1;
  size_t size = *(int*)actorFile;
  key_player player_info;
  player_info.base = (void *)actorFile;
  player_info.name = player;

  // getting offset using bsearch function
  void* offset = bsearch(&player_info, base, size, sizeof(int), compare_players);

  // return false if actor/actress is not in the database
  if(!offset) return false;

  // get to the byte where the number of the films is stored and read it
  int n_bytes = *(int*)offset;
  if(player.length() % 2 == 0) n_bytes += player.length() + 2;
  else n_bytes += player.length() + 1;
  void* byte_after_name = (char *)actorFile + n_bytes;
  short n_films = *(short*)byte_after_name;
  void* byte_after_n_films = (char*)byte_after_name + 2 + (n_bytes - *(int*)offset + 2) % 4;

  // read names and release dates of the films and store them in the vector
  for(int i = 0; i < n_films; i++) {
    int movie_offset = *((int*)byte_after_n_films + i);
    void* movie_index =  (char*)movieFile + movie_offset;
    char* movie_name = (char*)movie_index;
    string name(movie_name);
    movie_name += name.length() + 1;
    char year_delta = *movie_name;
    int year = 1900 + (int)year_delta;
    film current_film;
    current_film.title = name;
    current_film.year = year;
    films.push_back(current_film);
  }

  return true;
}


// compare function of movies for bsearch
int compare_movies(const void* movie1, const void* movie2) {
  key_movie m1 = *(key_movie*)movie1;
  film film1;
  film1.title = m1.name;
  film1.year = m1.year;
  void* base = m1.base;
  char* movie_index = (char*)base + *(int*)movie2;
  film film2;
  string title = string(movie_index);
  film2.title = title;
  movie_index += title.length() + 1;
  char delta_year = *(char*)movie_index;
  film2.year = 1900 + delta_year;
  //cout << film1.title << " - " << film2.title << endl;
  if(film1 < film2) return -1;
  else if(film1 == film2) return 0;
  else return 1;
}

bool imdb::getCast(const film& movie, vector<string>& players) const { 

  // defining some variables while we have an access to movieFile
  void* base = (int*)movieFile + 1;
  size_t size = *(int*)movieFile;
  key_movie movie_info;
  movie_info.base = (void*) movieFile;
  movie_info.name = movie.title;
  movie_info.year = movie.year;

  // getting offset using bsearch function
  void* offset = bsearch(&movie_info, base, size, sizeof(int), compare_movies);
  if(!offset) return false;

  // get to the byte where the number of actors/actresses are stored and read it
  void* movie_index = (char*)movieFile + ((*(int*)offset) + movie.title.length() + 2);
  void* number_byte;
  int bytes = movie.title.length() + 2;
  if(bytes % 2 == 0) {
    number_byte = movie_index;
  } else {
    number_byte = (char*)movie_index + 1;
    bytes++;
  }
  short n_actors = *(short*)number_byte;
  void* byte_after_n_actors;
  bytes += 2;
  if(bytes % 4 == 0) {
    byte_after_n_actors = (char*)number_byte + 2;
  } else byte_after_n_actors = (char*)number_byte + 4;
  
  // read actors/actresses of this particular film and store them in the vector
  for(int i = 0; i < n_actors; i++) {
    int actor_offset = *((int*)byte_after_n_actors + i);
    void* actor_index = (char*)actorFile + actor_offset;
    string name = string((char*)actor_index);
    players.push_back(name);
  }
  return true; 
}


imdb::~imdb()
{
  releaseFileMap(actorInfo);
  releaseFileMap(movieInfo);
}

// ignore everything below... it's all UNIXy stuff in place to make a file look like
// an array of bytes in RAM.. 
const void *imdb::acquireFileMap(const string& fileName, struct fileInfo& info)
{
  struct stat stats;
  stat(fileName.c_str(), &stats);
  info.fileSize = stats.st_size;
  info.fd = open(fileName.c_str(), O_RDONLY);
  return info.fileMap = mmap(0, info.fileSize, PROT_READ, MAP_SHARED, info.fd, 0);
}

void imdb::releaseFileMap(struct fileInfo& info)
{
  if (info.fileMap != NULL) munmap((char *) info.fileMap, info.fileSize);
  if (info.fd != -1) close(info.fd);
}
