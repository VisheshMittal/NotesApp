package com.vm.vishunotesapp.repository

import androidx.lifecycle.LiveData
import com.vm.vishunotesapp.models.UserRequest
import com.vm.vishunotesapp.models.UserResponse
import com.vm.vishunotesapp.utils.NetworkResult

/**
 * Repository layer for performing any data-operations related to "user".
 */
interface UserRepository {
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
    suspend fun signUpUser(userRequest: UserRequest)
    suspend fun signInUser(userRequest: UserRequest)
}