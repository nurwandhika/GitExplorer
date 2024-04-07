package com.example.fundamentalgithub.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fundamentalgithub.data.model.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 3, exportSchema = true)
abstract class FavoriteRoomDatabase : RoomDatabase() {
    abstract fun favDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRoomDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): FavoriteRoomDatabase = run {
            if (INSTANCE == null) {
                synchronized(FavoriteRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteRoomDatabase::class.java, "DBGithub"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            INSTANCE as FavoriteRoomDatabase
        }
    }
}