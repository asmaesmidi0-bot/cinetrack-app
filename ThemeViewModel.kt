package com.asma.cinetrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asma.cinetrack.data.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val userDataStore: UserDataStore
) : ViewModel() {

    val darkTheme: StateFlow<Boolean> = userDataStore.darkTheme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun setDarkTheme(darkTheme: Boolean) {
        viewModelScope.launch {
            userDataStore.setDarkTheme(darkTheme)
        }
    }
}
