package com.example.themovieapp

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.themovieapp.network.Movie
import com.example.themovieapp.search.MovieListAdapter


/**
 * When there is no Movie data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Movie>?) {
    val adapter = recyclerView.adapter as MovieListAdapter
    Log.d("listData binding", "${data}")
    adapter.submitList(data)
}


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, posterPath: String?) {
    val imgUrl = "image.tmdb.org/t/p/w500" + posterPath
    imgUrl.let {

        val imgUri = imgUrl.toUri()
            .buildUpon().scheme("https").build()
        Log.d("imgUri", imgUri.toString())
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}

@BindingAdapter("averageRating")
fun bindratingBackground(textView: TextView, avgRating: Double?) {
    textView.setText(avgRating.toString())
    if (avgRating != null) {
        when {
            avgRating >= 5 && avgRating < 6.5 ->
                textView.setBackgroundColor(
                    ContextCompat.getColor(
                        textView.context , R.color.ColorRatingMediumLow)
                )
            avgRating >= 6.5 && avgRating < 8 ->
                textView.setBackgroundColor(
                    ContextCompat.getColor(
                        textView.context , R.color.ColorRatingMediumHigh)
                )
            avgRating >= 8 ->
                textView.setBackgroundColor(
                    ContextCompat.getColor(
                        textView.context , R.color.ColorRatingHigh)
                )
            else ->
                textView.setBackgroundColor(
                    ContextCompat.getColor(
                        textView.context , R.color.ColorRatingLow)
                )

        }
    }

}


@BindingAdapter("showOnlyWhenEmpty")
fun View.showOnlyWhenEmpty(data: List<Movie>?) {
    visibility = when {
        data == null || data.isEmpty() -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("showOnlyWhenFull")
fun View.showOnlyWhenFull(data: List<Movie>?) {
    visibility = when {
        data == null || data.isEmpty() ->
            View.INVISIBLE
        else -> View.VISIBLE

    }
}