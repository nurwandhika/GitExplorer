package com.example.fundamentalgithub.data.remote

import com.example.fundamentalgithub.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GithubApiSetup {
    fun create(): GithubUserApiService {
        val client = createHttpClient()
        val retrofit = createRetrofit(client)
        return retrofit.create(GithubUserApiService::class.java)
    }

    fun createSearchApiService(): GithubSearchApiService {
        val client = createHttpClient()
        val retrofit = createRetrofit(client)
        return retrofit.create(GithubSearchApiService::class.java)
    }

    private fun createHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val authInterceptor = createAuthInterceptor()

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .addInterceptor(createErrorHandlingInterceptor())
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    private fun createAuthInterceptor() = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "token ${BuildConfig.TOKEN}")
            .build()
        chain.proceed(newRequest)
    }

    private fun createErrorHandlingInterceptor() = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)

        when (response.code) {
            403 -> throw Exception("Rate limit exceeded")
            401 -> throw Exception("Invalid token")
        }

        response
    }

    private fun createRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}