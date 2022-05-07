package com.example.madcw2

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovie( movie : Movies)

    @Query("SELECT * FROM MAD_Movie_Table")
    suspend fun readAllMovies(): List<Movies>

    @Query("SELECT * FROM MAD_Movie_Table WHERE actors LIKE '%' || :search || '%'")
    suspend fun readByActor(search: String): List<Movies>

    @Query("DELETE FROM MAD_Movie_Table")
    suspend fun deleteAll()
}