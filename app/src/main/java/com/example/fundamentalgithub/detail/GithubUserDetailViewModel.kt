package com.example.fundamentalgithub.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundamentalgithub.data.remote.GithubApiSetup
import com.example.fundamentalgithub.utils.ResultSealed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class GithubUserDetailViewModel: ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    val resultDetailUser = MutableLiveData<ResultSealed>()
    val resultFollowersUser = MutableLiveData<ResultSealed>()
    val resultFollowingUser = MutableLiveData<ResultSealed>()

    fun getDetailUser(username: String){
        makeApiCall(
            apiCall = { GithubApiSetup.create().fetchGithubUserDetails(username) },
            resultLiveData = resultDetailUser
        )
    }

    private var followersJob: Job? = null
    private var followingJob: Job? = null

    fun getGithubFollowers(username: String){
        followersJob?.cancel()
        followersJob = makeApiCall(
            apiCall = { GithubApiSetup.create().fetchGithubUserFollowers(username) },
            resultLiveData = resultFollowersUser
        )
    }

    fun getGithubFollowing(username: String){
        followingJob?.cancel()
        followingJob = makeApiCall(
            apiCall = { GithubApiSetup.create().fetchGithubUserFollowing(username) },
            resultLiveData = resultFollowingUser
        )
    }

    private fun makeApiCall(apiCall: suspend () -> Any, resultLiveData: MutableLiveData<ResultSealed>): Job {
        return viewModelScope.launch {
            flow {
                val response = apiCall()
                emit(response)
            }.onStart {
                setLoading(resultLiveData, true)
            }.onCompletion {
                setLoading(resultLiveData, false)
            }.catch {
                it.printStackTrace()
                setError(resultLiveData, it)
            }.collect {
                setSuccess(resultLiveData, it)
            }
        }
    }

    private fun setLoading(resultLiveData: MutableLiveData<ResultSealed>, isLoading: Boolean) {
        resultLiveData.value = ResultSealed.Loading(isLoading)
    }

    private fun setError(resultLiveData: MutableLiveData<ResultSealed>, exception: Throwable) {
        resultLiveData.value = ResultSealed.Error(exception)
    }

    private fun setSuccess(resultLiveData: MutableLiveData<ResultSealed>, data: Any) {
        resultLiveData.value = ResultSealed.Success(data)
    }
}
