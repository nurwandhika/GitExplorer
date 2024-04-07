package com.example.fundamentalgithub.detail

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.fundamentalgithub.R
import com.example.fundamentalgithub.data.model.UserList
import com.example.fundamentalgithub.data.model.FavoriteEntity
import com.example.fundamentalgithub.data.repo.FavoriteRepository
import com.example.fundamentalgithub.databinding.ActivityDetailBinding
import com.example.fundamentalgithub.detail.follow.GithubUserFollowFragment
import com.example.fundamentalgithub.utils.ResultSealed
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GithubUserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<GithubUserDetailViewModel>()
    private val repository: FavoriteRepository by lazy {
        FavoriteRepository(application)
    }

    private var isFavorite = false
    private var userDetails: UserList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra("username") ?: ""

        checkFavorite(username)
        setupFavoriteButton(username)
        setupObserver(username)
        setupViewPager(username)
    }

    private fun setupObserver(username: String) {
        viewModel.resultDetailUser.observe(this) {
            when (it) {
                is ResultSealed.Success<*> -> {
                    userDetails = it.data as UserList
                    setupUserDetails()
                }

                is ResultSealed.Error -> {
                    showToast(it.exception.message.toString())
                }

                is ResultSealed.Loading -> {
                    binding.detailProgressBar.isVisible = it.isLoading
                }
            }
        }
        viewModel.getDetailUser(username)
    }

    private fun setupUserDetails() {
        userDetails?.let { user ->
            binding.userAvatarImageView.load(user.avatar_url) {
                transformations(CircleCropTransformation())
            }
            binding.userNameTextView.text = user.name
            binding.userRealNameTextView.text = user.login
            binding.followersTextView.text = getString(R.string.followers_count, user.followers)
            binding.followingTextView.text = getString(R.string.following_count, user.following)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupViewPager(username: String) {
        val fragments = createFragments()
        val titleFragments = createTitleFragments()
        setupAdapter(fragments)
        setupTabLayoutMediator(titleFragments)
        setupTabSelectedListener(username)
    }

    private fun createFragments(): MutableList<Fragment> {
        return mutableListOf(
            GithubUserFollowFragment.newInstance(GithubUserFollowFragment.FOLLOWERS),
            GithubUserFollowFragment.newInstance(GithubUserFollowFragment.FOLLOWING)
        )
    }

    private fun createTitleFragments(): MutableList<String> {
        return mutableListOf(
            getString(R.string.followers), getString(R.string.following),
        )
    }

    private fun setupAdapter(fragments: MutableList<Fragment>) {
        val adapter = GithubUserDetailAdapter(this, fragments)
        binding.detailViewPager.adapter = adapter
    }

    private fun setupTabLayoutMediator(titleFragments: MutableList<String>) {
        TabLayoutMediator(binding.detailTabLayout, binding.detailViewPager) { table, position ->
            table.text = titleFragments[position]
        }.attach()
    }

    private fun setupTabSelectedListener(username: String) {
        binding.detailTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    viewModel.getGithubFollowers(username)
                } else {
                    viewModel.getGithubFollowing(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupFavoriteButton(username: String) {
        checkFavorite(username)
        binding.favoriteFab.apply {
            isVisible = true
            setOnClickListener {
                if (isFavorite) {
                    deleteFavorite(username)
                    setImageResource(R.drawable.ic_favorite_white)
                } else {
                    userDetails?.let { user ->
                        insertFavorite(username, user.avatar_url, user.html_url)
                        setImageResource(R.drawable.ic_favorite_red)
                    }
                }
                invalidate()
            }
        }
    }

    private fun insertFavorite(username: String, avatarUrl: String, htmlUrl: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            repository.insertFavorite(FavoriteEntity(login = username, avatarUrl = avatarUrl, htmlUrl = htmlUrl))
            withContext(Dispatchers.Main) {
                showToast("Profile Berhasil Ditambahkan ke Favorite")
                checkFavorite(username)
            }
        }
    }

    private fun deleteFavorite(username: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            repository.deleteFavorite(username)
            withContext(Dispatchers.Main) {
                showToast("Profile Berhasil Dihapus dari Favorite")
                checkFavorite(username)
            }
        }
    }

    private fun checkFavorite(username:String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val favoriteList = repository.checkFavorite(username).asFlow().first()
            val isFavorite = favoriteList.isNotEmpty()
            withContext(Dispatchers.Main) {
                this@GithubUserDetailActivity.isFavorite = isFavorite
                if (isFavorite) {
                    binding.favoriteFab.setImageResource(R.drawable.ic_favorite_red)
                } else {
                    binding.favoriteFab.setImageResource(R.drawable.ic_favorite_white)
                }
                binding.favoriteFab.invalidate()
            }
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