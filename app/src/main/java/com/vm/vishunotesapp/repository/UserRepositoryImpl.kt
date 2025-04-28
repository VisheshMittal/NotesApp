package com.vm.vishunotesapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vm.vishunotesapp.models.UserRequest
import com.vm.vishunotesapp.models.UserResponse
import com.vm.vishunotesapp.networking.UserApi
import com.vm.vishunotesapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

/**
 * Network-call based implementation of [UserRepository] interface.
 * @param userApi [UserApi] Retrofit instance to make network calls.
 */
class UserRepositoryImpl @Inject constructor(private val userApi: UserApi) : UserRepository {
    companion object {
        const val TAG = "UserRepositoryImpl"
    }

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    override val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    override suspend fun signUpUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val signUpResponse = userApi.signup(userRequest)

        handleResponse(signUpResponse)
        Log.i(TAG, "signUpUser response: ${signUpResponse.body().toString()}")
    }

    private fun handleResponse(userResponse: Response<UserResponse>) {
        if (userResponse.isSuccessful && userResponse.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(userResponse.body()!!))
        } else if (userResponse.errorBody() != null) {
            val userResponseJson = JSONObject(userResponse.errorBody()!!.string())
            _userResponseLiveData.postValue(
                NetworkResult.Error(userResponseJson.getString("message"))
            )
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Unknown error occurred"))
        }
    }

    override suspend fun signInUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val signInResponse = userApi.signin(userRequest)
        handleResponse(signInResponse)
        Log.i(TAG, "signInUser response : ${signInResponse.body().toString()}")
    }
}