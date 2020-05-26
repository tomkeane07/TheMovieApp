package com.example.themovieapp.testUtils

import com.example.themovieapp.db.DatabaseMovie
import com.example.themovieapp.domain.Movie

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
        return this.movie
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
                id = this.movie.id,
                title = this.movie.title,
                vote_average = this.movie.vote_average,
                poster_path = this.movie.poster_path,
                overview = this.movie.overview,
                adult = this.movie.adult,
                release_date = this.movie.release_date
            )
    }
}