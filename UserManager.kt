package com.asma.cinetrack.data

import android.content.Context
import android.content.SharedPreferences

class UserManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_EMAIL = "key_email"
        private const val KEY_NAME = "key_name"
        private const val KEY_IS_LOGGED_IN = "key_is_logged_in"
    }

    fun saveUser(email: String, name: String) {
        prefs.edit().apply {
            putString(KEY_EMAIL, email)
            putString(KEY_NAME, name)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getUserEmail(): String? {
        return prefs.getString(KEY_EMAIL, null)
    }

    fun getUserName(): String? {
        return prefs.getString(KEY_NAME, null)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    fun logout() {
        prefs.edit().clear().apply()
    }
}
