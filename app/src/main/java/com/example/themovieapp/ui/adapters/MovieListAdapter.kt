package com.example.themovieapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.themovieapp.databinding.MovieListItemBinding
import com.example.themovieapp.framework.domain.Movie


/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 */
class MovieListAdapter(val clickListener: MovieClickListener) :
    ListAdapter<Movie, MovieListAdapter.MovieListViewHolder>(
        DiffCallback
    ) {




    /**
     * Allows the RecyclerView to determine which items have changed when the [List]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem === newItem
        }

        /**
         * Create new [RecyclerView] item views (invoked by the layout manager)
         */
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }
    }



    /**
     * The MovieListViewHolder constructor takes the binding variable from the associated
     * MovieList, which nicely gives it access to the full [MovieList] information.
     */
    class MovieListViewHolder(
        private var binding: MovieListItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: MovieClickListener, movie: Movie) {

            binding.clickListener = clickListener
            binding.movie = movie
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MovieListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieListItemBinding.inflate(layoutInflater, parent, false)
                return MovieListViewHolder(
                    binding
                )
            }
        }
    }


    override fun onBindViewHolder(
        holder: MovieListViewHolder, position: Int
    ) {

        val movie = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(movie)
        }
        holder.bind(clickListener, movie)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MovieListViewHolder {
        return MovieListViewHolder.from(
            parent
        )
    }

}

class MovieClickListener(val clickListener: (movie: Movie) -> Unit) {
    fun onClick(movie: Movie) = clickListener(movie)
}

/*
val recyclerView = findViewById(R.id.myView)
recyclerView.getViewTreeObserver()
.addOnGlobalLayoutListener(object:ViewTreeObserver.OnGlobalLayoutListener() {
    fun onGlobalLayout() {
        //At this point the layout is complete and the
        //dimensions of recyclerView and any child views are known.
        //Remove listener after changed RecyclerView's height to prevent infinite loop
        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this)
    }
})*/


