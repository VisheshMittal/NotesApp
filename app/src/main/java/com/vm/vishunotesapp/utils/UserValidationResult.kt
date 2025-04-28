package com.vm.vishunotesapp.utils

sealed class UserValidationResult {
    class Success : UserValidationResult()
    class Error(val message: String) : UserValidationResult()
}