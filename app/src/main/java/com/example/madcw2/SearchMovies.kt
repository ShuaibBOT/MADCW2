package com.example.madcw2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.NullPointerException

import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL


class SearchMovies : AppCompatActivity() {
    private var attributeMovie: Array<String> = arrayOf("Title","Year","Rated","Released",
        "Runtime","Genre","Director","Writer",
        "Actors","Plot")
    private val attributeData = Array<String>(10){""}
    private lateinit var displayMovieView: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movies)


        val movieNameSearchField = findViewById<EditText>(R.id.movieNameSearchField)
        movieNameSearchField.movementMethod = ScrollingMovementMethod()
        val retrieveMovieBtn = findViewById<Button>(R.id.retrieveMovieBtn)
        val saveMovieToDatabaseBtn = findViewById<Button>(R.id.saveMovieToDBBtn)
        displayMovieView = findViewById(R.id.displayMovieView)

        var storeOnce = true
        while(savedInstanceState != null && storeOnce){
            val savedMovieInfo = savedInstanceState.getString("SEARCHED_MOVIE_INFO")
            displayMovieView.text=savedMovieInfo
            attributeData[0]= savedInstanceState.getString("SEARCHED_MOVIE_TITLE").toString()
            Log.d("Receive attribute 0",attributeData[0])
            attributeData[1]= savedInstanceState.getString("SEARCHED_MOVIE_YEAR").toString()
            attributeData[2]= savedInstanceState.getString("SEARCHED_MOVIE_RATED").toString()
            attributeData[3]= savedInstanceState.getString("SEARCHED_MOVIE_RELEASED").toString()
            attributeData[4]= savedInstanceState.getString("SEARCHED_MOVIE_RUNTIME").toString()
            attributeData[5]= savedInstanceState.getString("SEARCHED_MOVIE_GENRE").toString()
            attributeData[6]= savedInstanceState.getString("SEARCHED_MOVIE_DIRECTOR").toString()
            attributeData[7]= savedInstanceState.getString("SEARCHED_MOVIE_WRITER").toString()
            attributeData[8]= savedInstanceState.getString("SEARCHED_MOVIE_ACTOR").toString()
            attributeData[9]= savedInstanceState.getString("SEARCHED_MOVIE_PLOT").toString()

            storeOnce=false
        }



        retrieveMovieBtn.setOnClickListener {
            //receive movie name
            val movieName = movieNameSearchField.text.toString()
            Log.d("Movie Name",movieName)
            //Collecting JSON String
            val stb = StringBuilder()
            if(movieName.isEmpty()){
                Toast.makeText(applicationContext,"Movie Cannot be found",Toast.LENGTH_SHORT).show()
            }
            else{
                val urlString = "https://www.omdbapi.com/?t=$movieName&apikey=f531674f"
                val url = URL(urlString)
                Log.d("Print URL",url.toString())
                val con: HttpURLConnection = url.openConnection() as HttpURLConnection

                runBlocking {
                    launch {
                        withContext(Dispatchers.IO) {
                            Log.d("Enter runBLocking", "yes")
                            val bf = BufferedReader(InputStreamReader(con.inputStream))

                            var line: String = bf.readLine()
                            Log.d("lineOutput", line)
                            var proceed = true
                            var c = 0
                            while (line!=null && proceed == true) {
                                c+=1
                                Log.d("Enter", "Enter: "+c)
                                stb.append(line + "\n")
                                Log.d("test","after stb append")
                                try{
                                    line =bf.readLine();
                                }catch (e:NullPointerException){
                                    proceed= false;
                                }

                                Log.d("lineOutput", line)
                            }
                            Log.d("Print stb",stb.toString())
                            parseJSON(stb)
                            displayMovieView.text = ""
                            var j=0
                            for (i in attributeMovie) {
                                displayMovieView.append(i + ": " + attributeData[j] + "\n")
                                j+=1
                            }
                        }
                    }
                }
            }



        }

        saveMovieToDatabaseBtn.setOnClickListener {
            //Instantiate the DB
            val movieDB = Room.databaseBuilder(this,MovieDatabase::class.java,"Movie_Database").build()
            val movieDOA = movieDB.MovieDoa()
            runBlocking {
                launch {
                    val movie = Movies(attributeData[0],attributeData[1],attributeData[2],attributeData[3],attributeData[4],attributeData[5],
                        attributeData[6],attributeData[7],attributeData[8],attributeData[9])
                    movieDOA.addMovie(movie)
                }
            }
        }


    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var searchedMovieInfo = displayMovieView.text.toString()

        outState.putString("SEARCHED_MOVIE_INFO",searchedMovieInfo)
        outState.putString("SEARCHED_MOVIE_TITLE",attributeData[0])
        Log.d("Push attribute 0",attributeData[0])
        outState.putString("SEARCHED_MOVIE_YEAR",attributeData[1])
        outState.putString("SEARCHED_MOVIE_RATED",attributeData[2])
        outState.putString("SEARCHED_MOVIE_RELEASED",attributeData[3])
        outState.putString("SEARCHED_MOVIE_RUNTIME",attributeData[4])
        outState.putString("SEARCHED_MOVIE_GENRE",attributeData[5])
        outState.putString("SEARCHED_MOVIE_DIRECTOR",attributeData[6])
        outState.putString("SEARCHED_MOVIE_WRITER",attributeData[7])
        outState.putString("SEARCHED_MOVIE_ACTOR",attributeData[8])
        outState.putString("SEARCHED_MOVIE_PLOT",attributeData[9])

    }
    fun parseJSON(stb:java.lang.StringBuilder){
        Log.d("Enters Function","yes")
        val json = JSONObject(stb.toString())
        Log.d("json output:", json.toString())
        var c=0;
        for( i in attributeMovie){
            Log.d("Value",i)
            attributeData[c] = json[i].toString()
            Log.d("Data",attributeData[c])
            c+=1
        }
    }
}