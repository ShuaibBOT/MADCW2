package com.example.madcw2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class SearchActor : AppCompatActivity() {
    private lateinit var printListMovieView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_actor)

        val actorSearchField = findViewById<EditText>(R.id.actorSearchField)
        printListMovieView = findViewById(R.id.printListMovieView)
        printListMovieView.movementMethod = ScrollingMovementMethod()
        val searchMovieByActor = findViewById<Button>(R.id.searchMovieByActorBtn)

        var storeOnce = true
        while(savedInstanceState != null && storeOnce){
            val savedMovieInfo = savedInstanceState.getString("SEARCHED_MOVIE_INFO_1")
            printListMovieView.text=savedMovieInfo
            storeOnce=false
        }

        searchMovieByActor.setOnClickListener {
            //Instantiate the DB
            val movieDB = Room.databaseBuilder(this,MovieDatabase::class.java,"Movie_Database").build()
            val movieDOA = movieDB.MovieDoa()
            printListMovieView.text =""

            var searchMovie= actorSearchField.text.toString()
            runBlocking {
                launch {
                    val movieList: List<Movies> = movieDOA.readByActor(search = searchMovie)
                    if (movieList.isEmpty()){
                        val message = "Couldn't Find Movies by Actor with that Name"
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    }else {
                        for (i in movieList) {
                            var info = ("\nTitle: ${i.title}\nYear: ${i.year}\nRated: ${i.rated}" +
                                    "\nReleased: ${i.released}\nRuntime: ${i.runtime}\nGenre: ${i.genre}\n" +
                                    "Director: ${i.director}\nWriter: ${i.writer}\nActors: ${i.actors}\nPlot: ${i.plot}\n\n")
                            Log.d("Found", info)
                            printListMovieView.append(
                                "\nTitle: ${i.title}\nYear: ${i.year}\nRated: ${i.rated}" +
                                        "\nReleased: ${i.released}\nRuntime: ${i.runtime}\nGenre: ${i.genre}\n" +
                                        "Director: ${i.director}\nWriter: ${i.writer}\nActors: ${i.actors}\nPlot: ${i.plot}\n\n"
                            )
                        }
                    }
                }
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val searchedMovieInfo = printListMovieView.text.toString()

        outState.putString("SEARCHED_MOVIE_INFO_1",searchedMovieInfo)
    }
}