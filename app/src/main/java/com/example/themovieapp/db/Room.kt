package com.example.themovieapp.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {
    @Query("select * from databasemovie")
    fun getMovies(): LiveData<List<DatabaseMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<DatabaseMovie>)

    @Query("delete from databasemovie")
    suspend fun deleteAll()
}

@Database(entities = [DatabaseMovie::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "movies_database"
    }
    abstract val movieDao: MovieDao
}

private lateinit var INSTANCE: MoviesDatabase

fun getDatabase(context: Context): MoviesDatabase {
    synchronized(MoviesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java,
                "movies"
            ).build()
        }
    }
    return INSTANCE
}