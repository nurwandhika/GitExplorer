package com.example.fundamentalgithub.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity class UserList(

    @field:ColumnInfo(name = "avatar_url")
    val avatar_url: String,

    @field:ColumnInfo(name = "followers")
    val followers: Int,

    @field:ColumnInfo(name = "following")
    val following: Int,

    @field:ColumnInfo(name = "html_url")
    val html_url: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @field:ColumnInfo(name = "login")
    val login: String,

    @field:ColumnInfo(name = "name")
    val name: String,
)
