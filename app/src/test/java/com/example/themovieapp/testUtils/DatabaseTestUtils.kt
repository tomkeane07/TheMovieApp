package com.example.themovieapp.testUtils

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.themovieapp.framework.db.MoviesDatabase

object DatabaseTestUtils {
    fun getTestDb() =
        Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MoviesDatabase::class.java
        ).build()
}