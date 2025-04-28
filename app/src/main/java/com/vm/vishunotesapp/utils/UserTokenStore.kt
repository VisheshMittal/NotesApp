package com.vm.vishunotesapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.vm.vishunotesapp.utils.Constants.SHARED_PREFS_NAME
import com.vm.vishunotesapp.utils.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserTokenStore @Inject constructor(@ApplicationContext private val appContext: Context) {

    /**
     * Saves the token to shared preferences
     */
    fun saveToken(token: String) {
        val editor = appContext.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE).edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Gets the token from shared preferences
     */
    fun getToken(): String? {
        return appContext
            .getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
            .getString(USER_TOKEN, null)
    }
}