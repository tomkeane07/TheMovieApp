package com.example.themovieapp

import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.themovieapp.network.Movie
import com.example.themovieapp.search.MovieApiStatus
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

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@BindingAdapter("averageRating")
fun bindratingBackground(textView: TextView, avgRating: Double?) {
    textView.setText(avgRating.toString())
    if (avgRating != null) {
        textView.setBackgroundResource(R.drawable.rounded_view_with_border)
        when {
            avgRating >= 5 && avgRating < 6.5 ->
                textView.background.mutate().setTint(
                    ContextCompat.getColor(
                        textView.context , R.color.ColorRatingMediumLow)
                )
            avgRating >= 6.5 && avgRating < 8 ->
                textView.background.mutate().setTint(
                    ContextCompat.getColor(
                        textView.context , R.color.ColorRatingMediumHigh)
                )
            avgRating >= 8 ->
                textView.background.mutate().setTint(
                    ContextCompat.getColor(
                        textView.context , R.color.ColorRatingHigh)
                )
            else ->
                textView.background.mutate().setTint(
                    ContextCompat.getColor(
                        textView.context , R.color.ColorRatingLow)
                )
        }
    }

}

@BindingAdapter("movieApiStatus")
fun bindStatus(statusImageView: ImageView,
               status: MovieApiStatus?) {
    when (status) {
        MovieApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        MovieApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        MovieApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
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