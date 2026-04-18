package com.asma.cinetrack.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.asma.cinetrack.ui.auth.PasswordFieldWithStrength
import com.asma.cinetrack.viewmodel.ProfileViewModel

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val changePasswordState by viewModel.changePasswordState.collectAsState()

    Scaffold(
        topBar = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            Text("Change Password", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = { Text("Current Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            PasswordFieldWithStrength(value = newPassword, onValueChange = { newPassword = it })
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm New Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = newPassword != confirmPassword
            )
            if (newPassword != confirmPassword) {
                Text("Passwords do not match", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { viewModel.changePassword(currentPassword, newPassword) },
                enabled = newPassword == confirmPassword && changePasswordState !is com.asma.cinetrack.viewmodel.ChangePasswordState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (changePasswordState is com.asma.cinetrack.viewmodel.ChangePasswordState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Text("Change Password")
                }
            }
            if (changePasswordState is com.asma.cinetrack.viewmodel.ChangePasswordState.Error) {
                Text(
                    (changePasswordState as com.asma.cinetrack.viewmodel.ChangePasswordState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            if (changePasswordState is com.asma.cinetrack.viewmodel.ChangePasswordState.Success) {
                Text("Password changed successfully!", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
