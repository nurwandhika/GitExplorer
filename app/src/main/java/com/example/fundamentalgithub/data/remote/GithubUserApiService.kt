package com.example.fundamentalgithub.data.remote

import com.example.fundamentalgithub.data.model.UserList
import com.example.fundamentalgithub.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubUserApiService {

    @GET("users")
    suspend fun fetchAllGithubUsers(): MutableList<User>

    @GET("users/{username}")
    suspend fun fetchGithubUserDetails(@Path("username") username:String): UserList

    @GET("users/{username}/followers")
    suspend fun fetchGithubUserFollowers(@Path("username") username:String): MutableList<User>

    @GET("users/{username}/following")
    suspend fun fetchGithubUserFollowing(@Path("username") username:String): MutableList<User>
}