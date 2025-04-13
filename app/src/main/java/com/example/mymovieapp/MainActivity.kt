package com.example.mymovieapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovieapp.adapter.MovieAdapter
import com.example.mymovieapp.databinding.ActivityMainBinding
import com.example.mymovieapp.room.MovieEntity
import com.example.mymovieapp.viewModel.MovieViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MovieViewModel by viewModels()
    private lateinit var adapter: MovieAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MovieAdapter(emptyList<MovieEntity>()) { movie: MovieEntity, isChecked: Boolean ->
        }
        binding.recyclerMovies.layoutManager = LinearLayoutManager(this)
        binding.recyclerMovies.adapter = adapter

        binding.fabAddMovie.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        viewModel.movies.observe(this, Observer { movies ->
            if (movies.isNullOrEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerMovies.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerMovies.visibility = View.VISIBLE
                adapter.updateMovies(movies)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                val ids = adapter.getSelectedIds()
                if (ids.isNotEmpty()) {
                    viewModel.deleteMovies(ids)
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}