package com.acadexia.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acadexia.network.ApiClient
import com.acadexia.network.LoginRequest
import com.acadexia.network.SignupRequest
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Acadexia",
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 16.dp))
        }

        if (!isLogin) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nom") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                loading = true
                error = ""
                scope.launch {
                    try {
                        val response = if (isLogin) {
                            ApiClient.apiService.login(LoginRequest(email, password))
                        } else {
                            ApiClient.apiService.signup(SignupRequest(name, email, password))
                        }

                        if (response.isSuccessful) {
                            val token = response.body()?.token ?: ""
                            ApiClient.setToken(token)
                            onLoginSuccess(token)
                        } else {
                            error = "Erreur d'authentification"
                        }
                    } catch (e: Exception) {
                        error = e.message ?: "Erreur"
                    } finally {
                        loading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            enabled = !loading
        ) {
            Text(if (isLogin) "Se connecter" else "S'inscrire")
        }

        TextButton(onClick = { isLogin = !isLogin }) {
            Text(if (isLogin) "Pas de compte? S'inscrire" else "Déjà inscrit? Se connecter")
        }
    }
}
