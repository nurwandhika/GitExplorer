package com.example.fundamentalgithub.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundamentalgithub.R
import com.example.fundamentalgithub.data.model.FavoriteEntity
import com.example.fundamentalgithub.databinding.ActivityFavoriteBinding
import com.example.fundamentalgithub.detail.GithubUserDetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel by viewModels<FavoriteViewModel> {
        FavoritesViewModelFactory(
            application
        )
    }

    private val favoriteUserAdapter = FavoriteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = ContextCompat.getString(this@FavoriteActivity, R.string.favorite)
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel.getAllFavorite().observe(this) {
            favoriteUserAdapter.setList(it as ArrayList<FavoriteEntity>)
        }

        favoriteUserAdapter.onUserClick = { user ->
            Intent(this, GithubUserDetailActivity::class.java).apply {
                putExtra("username", user.login)
                startActivity(this)
            }
        }

        binding.favoriteRecyclerView.apply {
            adapter = favoriteUserAdapter
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}