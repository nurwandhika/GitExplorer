package com.example.fundamentalgithub

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundamentalgithub.data.remote.GithubApiSetup
import com.example.fundamentalgithub.utils.ResultSealed
import com.example.fundamentalgithub.utils.SettingsPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(private val preferences: SettingsPreferences) : ViewModel() {

    val resultUser = MutableLiveData<ResultSealed>()

    fun getUser() {
        viewModelScope.launch {
            makeApiCall(
                apiCall = { GithubApiSetup.create().fetchAllGithubUsers() }
            )
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch {
            makeApiCall(
                apiCall = {
                    GithubApiSetup.createSearchApiService().searchUserGithub(
                        mapOf(
                            "q" to username,
                            "per_page" to "15"
                        )
                    )
                }
            )
        }
    }

    private suspend fun makeApiCall(apiCall: suspend () -> Any) {
        flow {
            val response = apiCall()
            emit(response)
        }.onStart {
            setLoading(true)
        }.onCompletion {
            setLoading(false)
        }.catch {
            logError(it)
            setError(it)
        }.collect {
            setSuccess(it)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        resultUser.value = ResultSealed.Loading(isLoading)
    }

    private fun setError(exception: Throwable) {
        resultUser.value = ResultSealed.Error(exception)
    }

    private fun setSuccess(data: Any) {
        resultUser.value = ResultSealed.Success(data)
    }

    private fun logError(exception: Throwable) {
        Log.e("Error", exception.message.toString())
        exception.printStackTrace()
    }

    fun getDarkMode() = preferences.getDarkMode()
}