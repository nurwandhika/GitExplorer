package com.example.fundamentalgithub

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundamentalgithub.data.model.GithubUserModel
import com.example.fundamentalgithub.data.model.User
import com.example.fundamentalgithub.detail.GithubUserDetailActivity
import com.example.fundamentalgithub.favorite.FavoriteActivity
import com.example.fundamentalgithub.settings.SettingsActivity
import com.example.fundamentalgithub.utils.ResultSealed
import com.example.fundamentalgithub.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        UserAdapter { user ->
            Intent(this, GithubUserDetailActivity::class.java).apply {
                putExtra("username", user.login)
                startActivity(this)
            }
        }
    }
    private val viewModel by viewModels<MainViewModel>() {
        MainViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        viewModel.getUser()
    }

    private fun setupUI() {
        setupDarkMode()
        setupRecyclerView()
        setupSearchView()
        observeUserResult()
    }

    private fun setupDarkMode() {
        viewModel.getDarkMode().observe(this) {
            if (it)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun setupRecyclerView() {
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.mainRecyclerView.setHasFixedSize(true)
        binding.mainRecyclerView.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text.toString()
                if (query.isNotEmpty()) {
                    viewModel.getUser(query)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun observeUserResult() {
        viewModel.resultUser.observe(this) {
            when (it) {
                is ResultSealed.Success<*> -> {
                    when (it.data) {
                        is List<*> -> {
                            adapter.setData(it.data as MutableList<User>)
                        }
                        is GithubUserModel -> {
                            val items = it.data.users
                            adapter.setData(items as MutableList<User>)
                        }
                        else -> {
                            Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                is ResultSealed.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is ResultSealed.Loading -> {
                    binding.mainProgressBar.isVisible = it.isLoading
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite -> {
                startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
            }

            R.id.menu_settings -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}