package com.example.fundamentalgithub.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fundamentalgithub.data.model.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE login =:login")
    fun deleteFavorite(login: String)

    @Query("SELECT * FROM favorite")
    fun getAllFavorite(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite WHERE login =:login")
    fun checkFavorite(login: String): LiveData<List<FavoriteEntity>>
}