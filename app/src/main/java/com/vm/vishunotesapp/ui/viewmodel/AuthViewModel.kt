package com.vm.vishunotesapp.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vm.vishunotesapp.models.UserRequest
import com.vm.vishunotesapp.repository.UserRepository
import com.vm.vishunotesapp.utils.Constants
import com.vm.vishunotesapp.utils.UserValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    val userResponseLiveData = userRepository.userResponseLiveData

    fun validateUserRequest(userRequest: UserRequest, isLoginScreen: Boolean = false): UserValidationResult {
        if (!Patterns.EMAIL_ADDRESS.matcher(userRequest.email).matches()) {
            return UserValidationResult.Error("Invalid email")
        } else if (userRequest.password.length < Constants.MIN_PASSWORD_LENGTH) {
            return UserValidationResult.Error("Password length should be at least 5 characters")
        } else if (!isLoginScreen && userRequest.username.isEmpty()) {
            return UserValidationResult.Error("Username cannot be empty")
        }
        return UserValidationResult.Success()
    }

    fun signUpUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.signUpUser(userRequest)
        }
    }

    fun signInUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.signInUser(userRequest)
        }
    }
}