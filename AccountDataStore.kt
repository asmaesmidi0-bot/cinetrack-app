
package com.asma.cinetrack.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PASSWORD = stringPreferencesKey("user_password")
        val USER_NAME = stringPreferencesKey("user_name")
        val LOGGED_IN_USER_EMAIL = stringPreferencesKey("logged_in_user_email")
    }

    suspend fun saveAccount(user: User) {
        dataStore.edit {
            val emailKey = PreferencesKeys.USER_EMAIL
            val passwordKey = PreferencesKeys.USER_PASSWORD
            val nameKey = PreferencesKeys.USER_NAME

            it[emailKey] = user.email
            it[passwordKey] = user.password
            it[nameKey] = user.name
        }
    }

    suspend fun getAccount(email: String): User? {
        val emailKey = PreferencesKeys.USER_EMAIL
        val passwordKey = PreferencesKeys.USER_PASSWORD
        val nameKey = PreferencesKeys.USER_NAME

        val preferences = dataStore.data.first()
        val storedEmail = preferences[emailKey]
        val storedPassword = preferences[passwordKey]
        val storedName = preferences[nameKey]

        return if (storedEmail == email && storedPassword != null && storedName != null) {
            User(name = storedName, email = storedEmail, password = storedPassword)
        } else {
            null
        }
    }

    suspend fun setLoggedInUser(email: String?) {
        dataStore.edit {
            if (email != null) {
                it[PreferencesKeys.LOGGED_IN_USER_EMAIL] = email
            } else {
                it.remove(PreferencesKeys.LOGGED_IN_USER_EMAIL)
            }
        }
    }

    val loggedInUserEmail: StateFlow<String?> = dataStore.data
        .map {
            it[PreferencesKeys.LOGGED_IN_USER_EMAIL]
        }
        .catch { exception ->
            if (exception is IOException) {
                emit(null)
            } else {
                throw exception
            }
        }
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }
}
