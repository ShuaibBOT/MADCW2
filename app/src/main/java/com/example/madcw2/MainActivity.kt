package com.example.madcw2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.room.Room

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addMovieBtn =  findViewById<Button>(R.id.AddMovieBtn)
        val searchMoviesBtn = findViewById<Button>(R.id.searchMovieBtn)
        val searchByActorBtn = findViewById<Button>(R.id.searchActorBtn)
        val seeDatabaseBtn = findViewById<ImageButton>(R.id.seeDataBase)




        addMovieBtn.setOnClickListener {
            //Instantiate the DB
            val movieDB = Room.databaseBuilder(this,MovieDatabase::class.java,"Movie_Database").build()
            val movieDOA = movieDB.MovieDoa()
            runBlocking{
                launch{
                    movieDOA.deleteAll()
                    val movie1 = Movies("The Shawshank Redemption","1994","R","14 Oct 1994",
                        "142 min","Drama","Frank Darabont","Stephen King, Frank Darabont",
                        "Tim Robbins, Morgan Freeman, Bob Gunton","Two imprisoned men bond over a number of years, finding solace " +
                                "and eventual redemption through acts of common decency.")
                    val movie2 = Movies("Batman: The Dark Knight Returns, Part 1","2012","PG-13","25 Sep 2012",
                        "76 min","Animation, Action, Crime, Drama, Thriller","Jay Oliva","Bob Kane (character created by: Batman), Frank Miller (comic book), Klaus Janson (comic book), Bob"+
                                "Goodman","Peter Weller, Ariel Winter, David Selby, Wade Williams","Batman has not been seen for ten years. A new breed"+
                                "of criminal ravages Gotham City, forcing 55-year-old Bruce Wayne back"+
                                "into the cape and cowl. But, does he still have what it takes to fight"+
                                "crime in a new era?")
                    val movie3 = Movies("Inception","2010","PG-13","16 Jul 2010","148 min",
                        "Action, Adventure, Sci-Fi","Christopher Nolan","Christopher Nolan",
                        "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page","A thief who steals corporate secrets through the use of"+
                                "dream-sharing technology is given the inverse task of planting an idea"+
                                "into the mind of a C.E.O., but his tragic past may doom the project"+
                                "and his team to disaster.")
                    val movie4 = Movies("The Matrix","1999","R","31 Mar 1999","136 min",
                        "Action, Sci-Fi","Lana Wachowski, Lilly Wachowski","Lilly Wachowski, Lana Wachowski","Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
                        "When a beautiful stranger leads computer hacker Neo to a"+
                                "forbidding underworld, he discovers the shocking truth--the life he"+
                                "knows is the elaborate deception of an evil cyber-intelligence.")

                    movieDOA.addMovie(movie1)
                    movieDOA.addMovie(movie2)
                    movieDOA.addMovie(movie3)
                    movieDOA.addMovie(movie4)
                    Toast.makeText(applicationContext,"Movies added to Database", Toast.LENGTH_SHORT).show()
                }
            }
            println("Movies added to DB")
        }

        searchMoviesBtn.setOnClickListener {
            val intent = Intent (this,SearchMovies::class.java)

            startActivity(intent)

        }

        searchByActorBtn.setOnClickListener {
            val intent = Intent(this,SearchActor::class.java)

            startActivity(intent)
        }

    }
}