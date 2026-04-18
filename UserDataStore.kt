package com.asma.cinetrack.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserDataStore(context: Context) {

    private val appContext = context.applicationContext

    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_PHOTO_URI = stringPreferencesKey("user_photo_uri")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PASSWORD = stringPreferencesKey("user_password")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
    }

    val user: Flow<User> = appContext.dataStore.data.map {
        val name = it[PreferencesKeys.USER_NAME] ?: "Mr. John Doe"
        val photoUri = it[PreferencesKeys.USER_PHOTO_URI]
        val email = it[PreferencesKeys.USER_EMAIL] ?: ""
        val password = it[PreferencesKeys.USER_PASSWORD] ?: ""
        User(name = name, photoUri = photoUri, email = email, password = password)
    }

    suspend fun saveUser(user: User) {
        appContext.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = user.name
            preferences[PreferencesKeys.USER_EMAIL] = user.email
            preferences[PreferencesKeys.USER_PASSWORD] = user.password
            if (user.photoUri != null) {
                preferences[PreferencesKeys.USER_PHOTO_URI] = user.photoUri
            } else {
                preferences.remove(PreferencesKeys.USER_PHOTO_URI)
            }
        }
    }

    val darkTheme: Flow<Boolean> = appContext.dataStore.data.map {
        it[PreferencesKeys.DARK_THEME] ?: true
    }

    suspend fun setDarkTheme(darkTheme: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME] = darkTheme
        }
    }
}
