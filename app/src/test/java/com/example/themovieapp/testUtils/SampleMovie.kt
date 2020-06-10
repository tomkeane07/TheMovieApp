package com.example.themovieapp.testUtils

import com.example.themovieapp.framework.db.DatabaseMovie
import com.example.themovieapp.framework.domain.Movie

object SampleMovie {
    var movie = Movie(
        "346",
        "Seven Samurai",
        9.0,
        "/8OKmBV5BUFzmozIC3pPWKHy17kx.jpg",
        "A samurai answers a village's request for protection after he falls on hard times." +
                " The town needs protection from bandits, so the samurai gathers six others to" +
                " help him teach the people how to defend themselves, and the villagers provide the soldiers with food." +
                " A giant battle occurs when 40 bandits attack the village.",
        false,
        "1954-04-26"
    )

    fun get(): Movie {
        return movie
    }
    fun setVoteAvg(avg: Double){
        movie = Movie(
            "346",
            "Seven Samurai",
            avg,
            "/8OKmBV5BUFzmozIC3pPWKHy17kx.jpg",
            "A samurai answers a village's request for protection after he falls on hard times." +
                    " The town needs protection from bandits, so the samurai gathers six others to" +
                    " help him teach the people how to defend themselves, and the villagers provide the soldiers with food." +
                    " A giant battle occurs when 40 bandits attack the village.",
            false,
            "1954-04-26"
        )
    }

    fun asDatabaseMovie(): DatabaseMovie {
            return DatabaseMovie(
                id = movie.id,
                title = movie.title,
                vote_average = movie.vote_average,
                poster_path = movie.poster_path,
                overview = movie.overview,
                adult = movie.adult,
                release_date = movie.release_date
            )
    }
}