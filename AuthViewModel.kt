package com.asma.cinetrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asma.cinetrack.data.AccountDataStore
import com.asma.cinetrack.data.User
import com.asma.cinetrack.ui.auth.PasswordStrength
import com.asma.cinetrack.ui.auth.getPasswordStrength
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object AwaitingVerification : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val accountDataStore: AccountDataStore
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            val initialEmail = accountDataStore.loggedInUserEmail.first()
            if (initialEmail != null) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            if (email.isBlank() || password.isBlank()) {
                _authState.value = AuthState.Error("Email and password cannot be empty.")
                return@launch
            }
            val user = accountDataStore.getAccount(email)
            if (user != null && user.password == password) {
                accountDataStore.setLoggedInUser(email)
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error("Invalid email or password.")
            }
        }
    }

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                _authState.value = AuthState.Error("All fields must be filled.")
                return@launch
            }
            if (getPasswordStrength(password) == PasswordStrength.TOO_SHORT) {
                _authState.value = AuthState.Error("Password is too short.")
                return@launch
            }
            val existingUser = accountDataStore.getAccount(email)
            if (existingUser == null) {
                accountDataStore.saveAccount(User(name = name, email = email, password = password))
                _authState.value = AuthState.AwaitingVerification
            } else {
                _authState.value = AuthState.Error("An account with this email already exists.")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            accountDataStore.clear()
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun resetState() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Unauthenticated
        }
    }
}
