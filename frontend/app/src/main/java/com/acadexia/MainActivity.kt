package com.acadexia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.acadexia.network.ApiClient
import com.acadexia.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AcadexiaApp()
                }
            }
        }
    }
}

@Composable
fun AcadexiaApp() {
    var currentScreen by remember { mutableStateOf("login") }
    var token by remember { mutableStateOf<String?>(null) }

    when {
        token == null -> {
            LoginScreen(onLoginSuccess = { newToken ->
                token = newToken
                currentScreen = "dashboard"
            })
        }
        else -> {
            when (currentScreen) {
                "dashboard" -> DashboardScreen(onNavigate = { screen ->
                    currentScreen = screen
                })
                "chat" -> ChatScreen()
                "courses" -> CoursesScreen()
                "tasks" -> TasksScreen()
                "progress" -> ProgressScreen()
                else -> DashboardScreen(onNavigate = { screen ->
                    currentScreen = screen
                })
            }
        }
    }
}
