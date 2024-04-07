package com.example.fundamentalgithub.data.repo

import android.app.Application
import com.example.fundamentalgithub.data.db.FavoriteDao
import com.example.fundamentalgithub.data.db.FavoriteRoomDatabase
import com.example.fundamentalgithub.data.model.FavoriteEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(
    application: Application,
    private val dao: FavoriteDao = FavoriteRoomDatabase.getInstance(application).favDao(),
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
) {
    fun insertFavorite(favorite: FavoriteEntity) {
        executorService.execute { dao.insertFavorite(favorite) }
    }

    fun deleteFavorite(login: String) {
        executorService.execute { dao.deleteFavorite(login) }
    }

    fun getAllFavorite() = dao.getAllFavorite()

    fun checkFavorite(login: String) = dao.checkFavorite(login)
}