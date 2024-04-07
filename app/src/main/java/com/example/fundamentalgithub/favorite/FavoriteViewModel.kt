package com.example.fundamentalgithub.favorite

import androidx.lifecycle.ViewModel
import com.example.fundamentalgithub.data.repo.FavoriteRepository

class FavoriteViewModel(private val repository: FavoriteRepository) : ViewModel() {
    fun getAllFavorite() = repository.getAllFavorite()
}