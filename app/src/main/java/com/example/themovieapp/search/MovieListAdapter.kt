package com.example.themovieapp.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.themovieapp.databinding.MovieListItemBinding
import com.example.themovieapp.network.Movie


class MovieListAdapter(): ListAdapter<Movie, MovieListAdapter.MovieListViewHolder>(DiffCallback){

    class MovieListViewHolder(
        private var binding: MovieListItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.movie = movie
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(
        holder: MovieListAdapter.MovieListViewHolder, position: Int) {

        val movie = getItem(position)
        holder.bind(movie)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieListAdapter.MovieListViewHolder {

        return MovieListViewHolder(MovieListItemBinding.inflate(
            LayoutInflater.from(parent.context)))
    }


}