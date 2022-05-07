package com.example.madcw2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MAD_Movie_Table")
data class Movies (@PrimaryKey  val title:String,
                   val year:String,
                   val rated:String,
                   val released:String,
                   val runtime:String,
                   val genre:String,
                   val director:String,
                   val writer:String,
                   val actors:String,
                   val plot:String)
