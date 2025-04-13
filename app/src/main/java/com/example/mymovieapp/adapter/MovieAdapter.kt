package com.example.mymovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymovieapp.R
import com.example.mymovieapp.room.MovieEntity

class MovieAdapter(
    private var movies: List<MovieEntity>,
    private val onCheckedChange: (movie: MovieEntity, isChecked: Boolean) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val selectedMovies = mutableSetOf<String>()

    fun getSelectedIds(): List<String> = selectedMovies.toList()

    fun updateMovies(newMovies: List<MovieEntity>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPoster: ImageView = itemView.findViewById(R.id.imgPoster)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvYear: TextView = itemView.findViewById(R.id.tvYear)
        val cbSelector: CheckBox = itemView.findViewById(R.id.cbSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.tvTitle.text = movie.title
        holder.tvYear.text = movie.year
        Glide.with(holder.imgPoster.context)
            .load(movie.posterUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.imgPoster)

        holder.cbSelector.setOnCheckedChangeListener(null)
        holder.cbSelector.isChecked = selectedMovies.contains(movie.imdbID)
        holder.cbSelector.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedMovies.add(movie.imdbID)
            } else {
                selectedMovies.remove(movie.imdbID)
            }
            onCheckedChange(movie, isChecked)
        }
    }
}
