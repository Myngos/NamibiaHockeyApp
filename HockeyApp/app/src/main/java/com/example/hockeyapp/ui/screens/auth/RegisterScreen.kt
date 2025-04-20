package com.example.hockeyapp.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hockeyapp.data.remote.FirebaseService
import com.example.hockeyapp.ui.componets.CommonButton
import com.example.hockeyapp.ui.componets.CommonTextField
import com.example.hockeyapp.ui.componets.CommonTopBar

import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val firebaseService = remember { FirebaseService() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    fun validateInputs(): Boolean {
        var valid = true

        if (email.isEmpty()) {
            emailError = "Email cannot be empty"
            valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
            valid = false
        } else {
            emailError = ""
        }

        if (password.isEmpty()) {
            passwordError = "Password cannot be empty"
            valid = false
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            valid = false
        } else {
            passwordError = ""
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordError = "Please confirm your password"
            valid = false
        } else if (confirmPassword != password) {
            confirmPasswordError = "Passwords do not match"
            valid = false
        } else {
            confirmPasswordError = ""
        }

        return valid
    }

    fun register() {
        if (!validateInputs()) return

        isLoading = true
        coroutineScope.launch {
            try {
                val user = firebaseService.registerUser(email, password)
                if (user != null) {
                    navController.navigate("team_list") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    snackbarHostState.showSnackbar("Registration failed. Please try again.")
                }
            } catch (e: Exception) {
                snackbarHostState.showSnackbar(e.message ?: "An error occurred during registration")
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = { CommonTopBar(title = "Register", onBackClick = { navController.popBackStack() }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create an Account",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Join the Namibia Hockey community",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            CommonTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                keyboardType = KeyboardType.Email,
                isError = emailError.isNotEmpty(),
                errorMessage = emailError
            )

            CommonTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError.isNotEmpty(),
                errorMessage = passwordError
            )

            CommonTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                imeAction = ImeAction.Done,
                isError = confirmPasswordError.isNotEmpty(),
                errorMessage = confirmPasswordError
            )

            Spacer(modifier = Modifier.height(24.dp))

            CommonButton(
                text = "Register",
                onClick = { register() },
                isLoading = isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text("Already have an account? Login")
            }
        }
    }
}