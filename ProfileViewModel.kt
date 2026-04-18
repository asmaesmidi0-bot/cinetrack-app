package com.asma.cinetrack.viewmodel

import android.app.Application
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.asma.cinetrack.BuildConfig
import com.asma.cinetrack.data.User
import com.asma.cinetrack.data.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

sealed class ChangePasswordState {
    object Idle : ChangePasswordState()
    object Loading : ChangePasswordState()
    object Success : ChangePasswordState()
    data class Error(val message: String) : ChangePasswordState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
    application: Application
) : AndroidViewModel(application) {

    private var tempImageUri: Uri? = null

    val user: StateFlow<User> = userDataStore.user.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = User(name = "", email = "", photoUri = null, password = "")
    )

    private val _changePasswordState = MutableStateFlow<ChangePasswordState>(ChangePasswordState.Idle)
    val changePasswordState: StateFlow<ChangePasswordState> = _changePasswordState.asStateFlow()

    fun onNameChange(name: String) {
        viewModelScope.launch {
            userDataStore.saveUser(user.value.copy(name = name))
        }
    }

    fun onPhotoChange(photoUri: Uri?) {
        viewModelScope.launch {
            userDataStore.saveUser(user.value.copy(photoUri = photoUri.toString()))
        }
    }

    fun getTempImageUri(): Uri? {
        val applicationContext = getApplication<Application>().applicationContext
        val tmpFile = File.createTempFile("profile_picture", ".png", applicationContext.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        tempImageUri = FileProvider.getUriForFile(applicationContext, "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
        return tempImageUri
    }

    fun onImageTaken() {
        onPhotoChange(tempImageUri)
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _changePasswordState.value = ChangePasswordState.Loading
            if (user.value.password != currentPassword) {
                _changePasswordState.value = ChangePasswordState.Error("Incorrect current password.")
                return@launch
            }
            userDataStore.saveUser(user.value.copy(password = newPassword))
            _changePasswordState.value = ChangePasswordState.Success
        }
    }
}
