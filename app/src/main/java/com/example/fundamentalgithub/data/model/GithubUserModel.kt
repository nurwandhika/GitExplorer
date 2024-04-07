package com.example.fundamentalgithub.data.model

import com.google.gson.annotations.SerializedName

data class GithubUserModel(
    @field:SerializedName("total_count")
    val totalCount: Int? = null,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean? = null,

    @field:SerializedName("items")
    val users: List<User?>? = null
)