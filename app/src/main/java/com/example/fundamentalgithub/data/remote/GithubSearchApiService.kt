package com.example.fundamentalgithub.data.remote

import com.example.fundamentalgithub.data.model.GithubUserModel
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface GithubSearchApiService {

    @GET("search/users")
    suspend fun searchUserGithub(@QueryMap params: Map<String, String>): GithubUserModel
}