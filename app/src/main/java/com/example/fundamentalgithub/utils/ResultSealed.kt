package com.example.fundamentalgithub.utils

sealed class ResultSealed {
    data class Success<out T>(val data: T) : ResultSealed()
    data class Error(val exception: Throwable) : ResultSealed()
    data class Loading(val isLoading: Boolean) : ResultSealed()
}